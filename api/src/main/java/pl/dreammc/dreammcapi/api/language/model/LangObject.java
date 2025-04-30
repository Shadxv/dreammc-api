package pl.dreammc.dreammcapi.api.language.model;

import pl.dreammc.dreammcapi.api.util.GradientUtil;
import pl.dreammc.dreammcapi.api.util.Symbol;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LangObject<T, V> {

    // <c>Test color <g><c>gradient <c>bold<\c> <\c>no-bold<\g><\c>

    private final T value;

    protected LangObject(T value) {
        this.value = value;
    }

    public abstract Builder getBuilder();

    public abstract class Builder {

        protected T text;
        private final Queue<String> colors;
        private final Queue<String> effects;
        private final Queue<List<String>> gradients;
        private final Queue<String> values;
        private final Queue<String> symbols;
        // since 1.21.4
        // private final Queue<String> shadows;

        private final Stack<String> colorStack;
        private final Stack<String> effectStack;
        private final Queue<String> gradientColors;
        // since 1.21.4
        // private final Stack<String> shadowStack;

        protected Builder() {
            this.text = value;
            this.colors = new PriorityQueue<>();
            this.effects = new PriorityQueue<>();
            this.gradients = new PriorityQueue<>();
            this.values = new PriorityQueue<>();
            this.symbols = new PriorityQueue<>();
            //this.shadows = new PriorityQueue<>();

            this.colorStack = new Stack<>();
            this.effectStack = new Stack<>();
            this.gradientColors = new PriorityQueue<>();
            //this.shadowStack = new Stack<>();
        }

        public Builder colorize(String... colors) {
            this.colors.addAll(Arrays.stream(colors).toList());
            return this;
        }

        public Builder effects(String... effects) {
            this.effects.addAll(Arrays.stream(effects).toList());
            return this;
        }

        public Builder gradients(List<String>... gradients) {
            this.gradients.addAll(Arrays.stream(gradients).toList());
            return this;
        }

        public Builder values(Object... values) {
            this.values.addAll(Arrays.stream(values).map(Object::toString).toList());
            return this;
        }

        public Builder symbols(Symbol... symbols) {
            this.values.addAll(Arrays.stream(symbols).map(Symbol::getSymbolChar).toList());
            return this;
        }

        @Deprecated(since = "Before 1.21.4")
        public Builder shadows(String... shadows) {
            //this.shadows.addAll(Arrays.stream(shadows).toList());
            return this;
        }

        private String applyValue() {
            String result = this.values.poll();
            return result == null ? "MISSING" : result;
        }

        private String applySymbol() {
            String result = this.symbols.poll();
            return result == null ? "MISSING" : result;
        }

        private void applyColor() {
            this.colorStack.push(this.colors.poll());
        }

        private List<String> applyGradient(){
            return this.gradients.poll();
        }

        private void applyEffect() {
            this.effectStack.push(this.effects.poll());
        }

        protected String applyValues(String s) {
            StringBuilder result = new StringBuilder();
            Matcher valueMatcher = Pattern.compile("%[vs]%").matcher(s);
            while (valueMatcher.find()) {
                switch (valueMatcher.group(0)) {
                    case "%v%" -> valueMatcher.appendReplacement(result, this.applyValue());
                    case "%s%" -> valueMatcher.appendReplacement(result, this.applySymbol());
                }
            }
            valueMatcher.appendTail(result);
            return result.toString();
        }

        private String buildEffects() {
            StringBuilder builder = new StringBuilder();
            for (String effect : this.effectStack) {
                builder.append(effect);
            }
            return builder.toString();
        }

        private String buildColor() {
            return (this.colorStack.isEmpty() ? "" : this.colorStack.getLast()) + this.buildEffects();
        }

        private String buildGradientColor() {
            return (this.gradientColors.isEmpty() ? "" : this.gradientColors.poll()) + this.buildEffects();
        }

        protected String processGradients(String s) {
            StringBuilder result = new StringBuilder();
            Matcher matcher = Pattern.compile("%g%((?:(?!%g%|%/g%).)*(?:%/g%|$))", Pattern.DOTALL).matcher(s);

            while (matcher.find()) {
                String text = matcher.group(0);
                if (text.endsWith("%/g%")) text = text.substring(0, text.length() - 4);
                if (text.startsWith("%g%")) text = text.substring(3);
                StringBuilder output = new StringBuilder();
                int gradientColorCount = 0;

                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) == '%') {
                        if (i < text.length() - 4) {
                            if (text.charAt(i+1) == '/' && text.charAt(i+3) == '%') {
                                output.append(text, i, i+4);
                                i += 3;
                                continue;
                            }
                        }
                        if (i < text.length() - 3) {
                            if (text.charAt(i+2) == '%') {
                                output.append(text, i, i+3);
                                i += 2;
                                continue;
                            }
                        }
                    }
                    output.append("%gc%").append(text.charAt(i));
                    gradientColorCount++;
                    matcher.appendReplacement(result, output.toString());
                }

                this.gradientColors.addAll(GradientUtil.generateColorGradient(gradientColorCount, this.applyGradient()));
            }
            matcher.appendTail(result);

            return result.toString();
        }

        protected String formatText(String s) {
            StringBuilder result = new StringBuilder();
            Matcher matcher = Pattern.compile("%/?[ce]|(gc)").matcher(s);
            while (matcher.find()) {
                switch (matcher.group(0)) {
                    case "%/c%" -> {
                        this.colorStack.pop();
                    }
                    case "%/e%" -> {
                        this.effectStack.pop();
                    }
                    case "%gc%" -> {
                        matcher.appendReplacement(result, this.buildGradientColor());
                    }
                    default -> {
                        matcher.appendReplacement(result, this.buildColor());
                    }
                }
            }
            matcher.appendTail(result);
            return result.toString();
        }

        protected String build(String s) {
            String result = s;
            result = this.applyValues(result);
            result = this.processGradients(result);
            return this.formatText(result);
        }

        public abstract T buildAsString();
        public abstract V buildAsComponent();
    }
}

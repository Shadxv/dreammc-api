package pl.dreammc.dreammcapi.velocity.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("unchecked")
public abstract class PluginConfiguration<T extends PluginConfiguration<?>> {

    protected PluginConfiguration() {}

    public abstract T getDefault();

    public void saveConfig(Path path) {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);
        // Get the root node representation
        Node rootNode = yaml.represent(this);

        try (Writer writer = Files.newBufferedWriter(path)) {
            yaml.serialize(rootNode, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

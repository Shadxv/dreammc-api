package pl.dreammc.dreammcapi.api.model;

import lombok.Getter;
import org.bson.Document;
import pl.dreammc.dreammcapi.api.manager.ProfileManager;
import pl.dreammc.dreammcapi.api.type.PlayerRank;
import pl.dreammc.dreammcapi.api.type.ProfileValueType;
import pl.dreammc.dreammcapi.api.util.FormatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileModel {

    @Getter private final UUID uuid;
    @Getter private PlayerRank permanentRank;
    @Getter private final List<TimeRankModel> timeRanks;
    @Getter private PlayerRank currentRank;
    @Getter private double wallet;
    @Getter private int coins;
    @Getter private int gems;


    public ProfileModel(UUID uuid) {
        this.uuid = uuid;
        this.permanentRank = PlayerRank.PLAYER;
        this.timeRanks = new ArrayList<>();
        this.currentRank = this.permanentRank;
        this.wallet = 0.0d;
        this.coins = 0;
        this.gems = 0;
    }

    public ProfileModel(UUID uuid, PlayerRank permanentRank, List<TimeRankModel> timeRanks, PlayerRank currentRank, double wallet, int coins, int gems) {
        this.uuid = uuid;
        this.permanentRank = permanentRank;
        this.timeRanks = timeRanks;
        this.currentRank = currentRank;
        this.wallet = wallet;
        this.coins = coins;
        this.gems = gems;
    }


    public Document toMongoDocument() {
        Document result = new Document()
                .append("uuid", this.uuid)
                .append("permanentRank", this.permanentRank.name());

        List<Document> timeRanks = new ArrayList<>();
        for(TimeRankModel rank : this.timeRanks) {
            timeRanks.add(rank.toMongoDocument());
        }

        result.append("timeRanks", timeRanks)
                .append("wallet", this.wallet)
                .append("coins", this.coins)
                .append("gems", this.gems);

        return result;
    }

    public static ProfileModel fromMongoDocument(Document document) {
        List<TimeRankModel> timeRanks = new ArrayList<>();
        for (Document rankDocument : document.getList("timeRanks", Document.class)) {
            timeRanks.add(TimeRankModel.fromMongoDocument(rankDocument));
        }

        return new ProfileModel(
                document.get("uuid", UUID.class),
                PlayerRank.valueOf(document.getString("permanentRank")),
                timeRanks,
                PlayerRank.PLAYER,
                document.getDouble("wallet"),
                document.getInteger("coins"),
                document.getInteger("gems")
        );
    }

    public void setWallet(double newValue) {
        this.wallet = newValue;
        ProfileManager.getInstance().sendUpdateToDatabase(this.getUuid(), "wallet", this.wallet);
        ProfileManager.getInstance().callChangeEvent(this.getUuid(), this, ProfileValueType.WALLET);
    }

    public void addToWallet(double delta) {
        this.setWallet(FormatUtil.roundToTwoDecimalPlaces(this.wallet + delta));
    }

    public void removeFromWallet(double delta) {
        this.setWallet(FormatUtil.roundToTwoDecimalPlaces(this.wallet - delta));
    }

    public void setCoins(int newValue) {
        this.coins = newValue;
        ProfileManager.getInstance().sendUpdateToDatabase(this.getUuid(), "coins", this.coins);
        ProfileManager.getInstance().callChangeEvent(this.getUuid(), this, ProfileValueType.COINS);
    }

    public void addCoins(int delta) {
        this.setCoins(this.coins + delta);
    }

    public void removeCoins(int delta) {
        this.setCoins(this.coins - delta);
    }

    public void setGems(int newValue) {
        this.gems = newValue;
        ProfileManager.getInstance().sendUpdateToDatabase(this.getUuid(), "gems", this.gems);
        ProfileManager.getInstance().callChangeEvent(this.getUuid(), this, ProfileValueType.GEMS);
    }

    public void addGems(int delta) {
        this.setGems(this.gems + delta);
    }

    public void removeGems(int delta) {
        this.setGems(this.gems - delta);
    }
}

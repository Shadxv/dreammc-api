package pl.dreammc.dreammcapi.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;
import pl.dreammc.dreammcapi.api.type.PlayerRank;

@AllArgsConstructor
@Getter
public class TimeRankModel {

    private final PlayerRank rank;
    private long expireTime;

    public Document toMongoDocument() {
        return new Document()
                .append("rank", this.rank.name())
                .append("expireTime", this.expireTime);
    }

    public static TimeRankModel fromMongoDocument(Document document) {
        return new TimeRankModel(
                PlayerRank.valueOf(document.getString("rank")),
                document.getLong("expireTime")
        );
    }

}

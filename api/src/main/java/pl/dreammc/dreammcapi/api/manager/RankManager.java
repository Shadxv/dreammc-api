package pl.dreammc.dreammcapi.api.manager;

import pl.dreammc.dreammcapi.api.model.TimeRankModel;
import pl.dreammc.dreammcapi.api.type.PlayerRank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RankManager {

    public static List<TimeRankModel> validateTimeRanks(UUID owner, List<TimeRankModel> timeRanks, PlayerRank permanentRank) {
        List<TimeRankModel> result = new ArrayList<>();

        long currentTime = System.currentTimeMillis();

        for(TimeRankModel rankModel : timeRanks) {
            if(currentTime <= rankModel.getExpireTime() | permanentRank.ordinal() >= rankModel.getRank().ordinal()) {
                removeTimeRank(owner, rankModel.getRank());
                continue;
            }

            result.add(rankModel);

            if(result.size() <= 1) {
                continue;
            }

            for(int i = 0; i < result.size(); i++) {
                if(rankModel.getRank().ordinal() >= result.get(i).getRank().ordinal()) {
                    result.add(i, rankModel);
                    result.removeLast();
                    break;
                }
            }
        }

        return result;
    }

    public static void removeTimeRank(UUID owner, PlayerRank rankType) {

    }

}

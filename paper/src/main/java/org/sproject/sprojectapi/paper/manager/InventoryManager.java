package org.sproject.sprojectapi.paper.manager;

import org.sproject.sprojectapi.paper.PaperSProjectAPI;

public class InventoryManager {



    public static InventoryManager getInstance() {
        return PaperSProjectAPI.getInstance().getInventoryManager();
    }
}

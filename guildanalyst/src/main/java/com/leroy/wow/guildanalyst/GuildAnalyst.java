package com.leroy.wow.guildanalyst;

import com.leroy.wow.battlenet.BattleNetClient;
import com.leroy.wow.beans.WowGuild;

public class GuildAnalyst {
    
    public static void main(String...args) throws Throwable{
        BattleNetClient client = new BattleNetClient("EU", "Sargeras");
        WowGuild guild = client.getGuild("La Meute");
        
        guild.getMembers()
            .stream()
            .map(m -> client.getSafeCharacter(m.getName()))
            .filter(c -> c != null)
            .sorted((c1, c2) -> c2.getAverageItemLevelEquipped().compareTo(c1.getAverageItemLevelEquipped()))
            .forEach(c -> System.out.println(c.getName() + "->" + c.getAverageItemLevelEquipped()));
    }
}

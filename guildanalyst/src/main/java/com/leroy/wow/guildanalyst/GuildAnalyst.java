package com.leroy.wow.guildanalyst;

import java.util.Comparator;

import com.leroy.wow.battlenet.BattleNetClient;
import com.leroy.wow.beans.WowCharacter;
import com.leroy.wow.beans.WowGuild;

public class GuildAnalyst {
    
    public static void main(String...args) throws Throwable{
        BattleNetClient client = new BattleNetClient("EU", "Sargeras");
        WowGuild guild = client.getGuild("La Meute");
        
        guild.getMembers()
            .stream()
            .map(m -> client.getSafeCharacter(m.getName()))
            .filter(c -> c != null)
            .sorted(Comparator.comparingLong(WowCharacter::getAverageItemLevelEquipped).reversed())
            .forEach(c -> System.out.println(c.getName() + "->" + c.getAverageItemLevelEquipped()));
    }
    
}

/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.s;

import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.effects.common.GainLifeEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Zone;
import mage.filter.StaticFilters;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.game.events.ZoneChangeEvent;
import mage.game.permanent.Permanent;

import java.util.UUID;

/**
 *
 * @author Plopman
 */
public class Scrapheap extends CardImpl {

    public Scrapheap(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ARTIFACT},"{3}");

        // Whenever an artifact or enchantment is put into your graveyard from the battlefield, you gain 1 life.
        this.addAbility(new ScrapheapTriggeredAbility());
    }

    public Scrapheap(final Scrapheap card) {
        super(card);
    }

    @Override
    public Scrapheap copy() {
        return new Scrapheap(this);
    }
}

class ScrapheapTriggeredAbility extends TriggeredAbilityImpl {

    @Override
    public ScrapheapTriggeredAbility copy() {
        return new ScrapheapTriggeredAbility(this);
    }
    
    public ScrapheapTriggeredAbility(final ScrapheapTriggeredAbility ability){
        super(ability);
    }
    
    public ScrapheapTriggeredAbility(){
       super(Zone.BATTLEFIELD, new GainLifeEffect(1));
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.ZONE_CHANGE;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        ZoneChangeEvent zEvent = (ZoneChangeEvent) event;
        if (zEvent.getFromZone() == Zone.BATTLEFIELD && zEvent.getToZone() == Zone.GRAVEYARD) {
            Permanent permanent = (Permanent) game.getLastKnownInformation(event.getTargetId(), Zone.BATTLEFIELD);
            if (permanent != null && permanent.getOwnerId().equals(this.getControllerId())) {
                if (StaticFilters.ARTIFACT_OR_ENCHANTMENT_PERMANENT.match(permanent, sourceId, controllerId, game)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public String getRule() {
        return "Whenever an artifact or enchantment is put into your graveyard from the battlefield, you gain 1 life.";
    }
}

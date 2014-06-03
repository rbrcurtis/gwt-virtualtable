package com.mut8ed.battlemap.client.widget.charactersheet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Weapon;
import com.mut8ed.battlemap.shared.dto.charactersheet.WeaponAttackBonus;
import com.mut8ed.battlemap.shared.dto.charactersheet.WeaponDamageBonus;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class WeaponsPanel extends FlexTable {

	private CharacterSheet cs;
	private Button adder;

	public WeaponsPanel(final CharacterSheet cs) {
		this.cs = cs;
		
		setStylePrimaryName("weaponsPanel");
		
		this.getRowFormatter().addStyleName(0, "FlexTable-Header");
		
		adder = new Button("+");
		
		showWeapons();
		
		
		adder.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				new AddWeaponPanel(cs, WeaponsPanel.this).center();
				
			}
		});
		
		
		
	}

	public void showWeapons() {
		
		clear(true);
		
		setHTML(0, 0, "Weapon");
		setHTML(0, 1, "ATK");
		setHTML(0, 2, "Range");
		setHTML(0, 3, "DMG");
		setWidget(0, 4, adder);
		
		for (final Weapon weapon : this.cs.getWeapons()){
			final int i = this.getRowCount();
			setHTML(i, 0, weapon.getName());
			setHTML(i, 1, weapon.getAttack());
			setHTML(i, 2, ""+weapon.getRange());
			setHTML(i, 3, weapon.getDamage());
			
			for (WeaponAttackBonus b : weapon.getAttackBonuses()){
				b.addWatcher(new Watcher() {
					
					@Override
					public void onChange() {
						setHTML(i, 1, weapon.getAttack());						
					}
				});
			}
			for (WeaponDamageBonus b : weapon.getDamageBonuses()){
				b.addWatcher(new Watcher() {
					
					@Override
					public void onChange() {
						setHTML(i, 3, weapon.getDamage());						
					}
				});
			}
			HTML remove = new HTML("&#10007;");
			remove.setWidth("20px");
			remove.setHeight("15px");
			DOM.setStyleAttribute(remove.getElement(), "cursor", "pointer");
			
			remove.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if (!Window.confirm("Are you sure you want to delete this weapon?  This cannot be undone."))return;
					cs.removeWeapon(weapon);
					CharacterView.getInstance().save();
					showWeapons();
				}
			});
			
			setWidget(i, 4, remove);
		}		
	}

}

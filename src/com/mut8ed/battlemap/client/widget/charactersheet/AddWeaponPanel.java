package com.mut8ed.battlemap.client.widget.charactersheet;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.MagicWeaponDamageBonus;
import com.mut8ed.battlemap.shared.dto.charactersheet.Weapon;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.CombatStatType;

public class AddWeaponPanel extends DialogBox {
	
	FlexTable content = new FlexTable();

	public AddWeaponPanel(final CharacterSheet cs, final WeaponsPanel wp) {
		super(true);
		
		setWidget(content);

		Label lblWeapon = new Label("Weapon");
		content.setWidget(0, 0, lblWeapon);

		final TextBox weaponName = new TextBox();
		content.setWidget(0, 1, weaponName);

		Label lblDamage = new Label("Damage");
		content.setWidget(0, 2, lblDamage);

		final TextBox damage = new TextBox();
		content.setWidget(0, 3, damage);
		damage.setWidth("40px");

		Label lblRange = new Label("Range");
		content.setWidget(0, 4, lblRange);

		final IntegerBox range = new IntegerBox();
		content.setWidget(0, 5, range);
		range.setWidth("20px");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		content.setWidget(1, 0, horizontalPanel);
		horizontalPanel.setWidth("100%");

		final RadioButton btn1h = new RadioButton("weaponType", "1H");
		horizontalPanel.add(btn1h);

		final RadioButton btn2h = new RadioButton("weaponType", "2H");
		horizontalPanel.add(btn2h);

		final RadioButton finesse = new RadioButton("weaponType", "Finesse");
		horizontalPanel.add(finesse);

		final RadioButton ranged = new RadioButton("weaponType", "Ranged");
		horizontalPanel.add(ranged);

		Label lblMaxStr = new Label("Max STR");
		content.setWidget(1, 1, lblMaxStr);

		final IntegerBox maxStr = new IntegerBox();
		content.setWidget(1, 2, maxStr);
		maxStr.setWidth("20px");
		content.getFlexCellFormatter().setColSpan(1, 0, 4);

		FlowPanel flowPanel = new FlowPanel();
		content.setWidget(2, 0, flowPanel);

		Label lblOtherBonuses = new Label("Other Bonuses");
		DOM.setStyleAttribute(lblOtherBonuses.getElement(), "display", "block");
		flowPanel.add(lblOtherBonuses);

		final List<TextBox> mods = new ArrayList<TextBox>();
		for (int i = 0 ; i < 4 ; i++){
			TextBox m = new TextBox();
			DOM.setStyleAttribute(m.getElement(), "display", "inline");
			mods.add(m);
			flowPanel.add(m);
			m.setWidth("60px");
		}

		Label lblMagic = new Label("Magic");
		content.setWidget(2, 1, lblMagic);

		final IntegerBox magicBns = new IntegerBox();
		content.setWidget(2, 2, magicBns);
		magicBns.setWidth("20px");

		Button submit = new Button("Submit");
		content.setWidget(3, 0, submit);
		content.getFlexCellFormatter().setColSpan(3, 0, 6);
		content.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_CENTER);
		content.getFlexCellFormatter().setColSpan(2, 0, 4);
		content.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);

		submit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				DOM.setStyleAttribute(weaponName.getElement(), "background", "");
				DOM.setStyleAttribute(damage.getElement(), "background", "");
				
				if (weaponName.getValue().equals("")){
					Window.alert("Please enter a weapon name");
					DOM.setStyleAttribute(weaponName.getElement(), "background", "red");
					return;
				}
				
				if (damage.getValue().equals("")){
					Window.alert("Please enter base damage");
					DOM.setStyleAttribute(damage.getElement(), "background", "red");
					return;
				}

				Weapon weapon = new Weapon(weaponName.getValue(), damage.getValue(), range.getValue());
				if (btn1h.getValue()){

					weapon.is2H(false);
					weapon.addAttackBonus(cs.getScore(CombatStatType.MELEE));
					weapon.addDamageBonus(cs.getAbility(Ability.Type.STR));

				} else if (btn2h.getValue()){

					weapon.is2H(true);
					weapon.addAttackBonus(cs.getScore(CombatStatType.MELEE));
					weapon.addDamageBonus(cs.getAbility(Ability.Type.STR));

				} else if (finesse.getValue()){

					weapon.addAttackBonus(cs.getScore(CombatStatType.RANGED));
					weapon.addDamageBonus(cs.getAbility(Ability.Type.STR));

				} else if (ranged.getValue()){

					weapon.addAttackBonus(cs.getScore(CombatStatType.RANGED));
					if (maxStr.getValue()!=null && maxStr.getValue()>0){
						weapon.addDamageBonus(cs.getAbility(Ability.Type.STR), maxStr.getValue());
					}
					
				}
				
				weapon.addDamageBonus(cs.getScore(CombatStatType.DMG));
				
				for (final TextBox mod : mods){
					if (mod.getValue().equals(""))continue;
					weapon.addDamageBonus(new MagicWeaponDamageBonus(mod.getValue()));
				}
				
				if (magicBns.getValue()!=null)weapon.setMagicBonus(magicBns.getValue());
				
				cs.addWeapon(weapon);
				wp.showWeapons();
				
				CharacterView.getInstance().save();
				
				hide();
			}
		});
		

	}

	@Override
	public boolean onKeyDownPreview(char key, int modifiers) {
		if (key == KeyCodes.KEY_ESCAPE){
			hide();
			return false;
		}
		
		return true;
	}
}

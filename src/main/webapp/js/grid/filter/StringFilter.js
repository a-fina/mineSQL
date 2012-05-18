/*
 * Ext JS Library 2.1
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.grid.filter.StringFilter = Ext.extend(Ext.grid.filter.Filter, {
	updateBuffer: 500,
	icon: '/img/find.png',
	
	init: function() {
		var value = this.value = new Ext.menu.EditableItem({icon: this.icon});
		value.on('keyup', this.onKeyUp, this);
		value.on('update', this.fireUpdate, this);
		this.menu.add(value);
		
		this.updateTask = new Ext.util.DelayedTask(this.fireUpdate, this);
	},
	
	onKeyUp: function(event) {
		if(event.getKey() == event.ENTER){
			this.menu.hide(true);
		    this.updateTask.delay(this.updateBuffer);
			return;
		}
	},

	isActivatable: function() {
		return this.value.getValue().length > 0;
	},
	
	fireUpdate: function() {
	//FIX: ALE	if(this.active) {
			this.fireEvent("update", this);
    //}
		this.setActive(this.isActivatable());
	},
	
	setValue: function(value) {
		this.value.setValue(value);
		this.fireEvent("update", this);
	},
	
	getValue: function() {
		return this.value.getValue();
	},
	
	serialize: function() {
		var args = {type: 'string', value: this.getValue()};
		this.fireEvent('serialize', args, this);
		return args;
	},
	
	validateRecord: function(record) {
		var val = record.get(this.dataIndex);
		if(typeof val != "string") {
			return this.getValue().length == 0;
    }
		return val.toLowerCase().indexOf(this.getValue().toLowerCase()) > -1;
	},
	
	listeners:{
		// Intercetta l'attivazione del filtro quando viene premuto
		// ENTER e l'EditableItem e' nascosto e precedentemente valorizzato
		'activate': function() {this.fireUpdate();}
	}
});

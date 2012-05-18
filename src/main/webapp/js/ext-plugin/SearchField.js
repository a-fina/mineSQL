/* 
 * Ext JS Library 2.0.2
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.app.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
    initComponent : function(){
        Ext.app.SearchField.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();
            }
        }, this);
    },

    validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
    hideTrigger1:false,
    width:180,
    hasSearch : false,
    paramName : 'query',
    currentFlusso: 'none',

    onTrigger1Click : function(){
        if(SessionFilters[this.currentFlusso][currentQuery] != "")
            this.hasSearch = true;

        if(this.hasSearch){
            this.el.dom.value = '';
            var o = {start: 0};
            this.store.baseParams = this.store.baseParams || {};
            this.store.baseParams[this.paramName] = '';
            SessionFilters[this.currentFlusso][currentQuery] = "";
            this.triggers[0].hide();
            this.hasSearch = false;
            this.store.reload({params:o});
        }
    },

    onTrigger2Click : function(){
        var v = this.getRawValue();
        // Salvo in sessione il filtro corrente
        if (this.currentFlusso != 'none'){
            SessionFilters[this.currentFlusso][currentQuery] = v;
        }
        if(v.length < 1){
            this.onTrigger1Click();
            return;
        }
        var o = {start: 0};
        this.store.baseParams = this.store.baseParams || {};
        this.store.baseParams[this.paramName] = v;
        this.store.reload({params:o});
        this.hasSearch = true;
        this.triggers[0].show();
    }
});

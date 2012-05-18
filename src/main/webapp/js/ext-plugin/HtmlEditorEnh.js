/**
 * Override per l'aggiunta dei metodi markInvalid, clearInvalid, isEmpty, isValid.
 * Il componente Ext.form.HtmlEditor non prevede i soliti meccanismi
 * lato client degli altri componenti Ext.form.*
 */
Ext.override(Ext.form.HtmlEditor, {
	// allowBlank e maxLength sono da gestire manualmente, non sono
	// previsti i soliti meccanismi di validazione
	allowBlank: true,
	maxLength: -1,
	invalidClass: 'x-htmleditor-invalid',
    markInvalid: function(msg){
        if(!this.rendered || this.preventMark){
            return;
        }
        msg = msg || this.invalidText;
        switch(this.msgTarget){
            case 'qtip':
                this.iframe.qtip = msg;
                this.iframe.qclass = 'x-form-invalid-tip';
                Ext.get(this.iframe).addClass(this.invalidClass);
                break;
        }
        return Ext.form.TextArea.superclass.markInvalid.call(this, [msg]);
    },
    clearInvalid: function(){
        if(!this.rendered || this.preventMark){
            return;
        }
        switch(this.msgTarget){
            case 'qtip':
                this.iframe.qtip = '';
                Ext.get(this.iframe).removeClass(this.invalidClass);
                break;
        }
        return Ext.form.TextArea.superclass.clearInvalid.call(this);
    },
    isEmpty: function(){
    	if (this.getValue().length == 0 ||
    		this.getValue() == '<br>' ||
    		this.getValue() == '<P>&nbsp;</P>')
    		return true;
    	else
    		return false;
    },
    isValid: function() {
    	if (!this.allowBlank && this.isEmpty()) {
    		this.markInvalid('This field is required');
    		return false;
    	}
    	else if (this.maxLength != -1 && this.getValue().length > this.maxLength) {
    		this.markInvalid('Errore: la lunghezza massima &eacute; di ' + this.maxLength + ' caratteri');
    		return false;
    	}
    	else {
    		this.clearInvalid();
    		return true;
    	}
    }
});
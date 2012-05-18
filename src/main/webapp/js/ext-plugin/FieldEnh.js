/**
 * Override per l'aggiunta del metodo setReadOnly.
 * Se displayOnly = true, il campo sara' readOnly e sara' visualizzato
 * utilizzando il css x-item-disabled se non diversamente specificato.
 */
Ext.override(Ext.form.Field, {
        setReadOnly: function(displayOnly,cls){
            // Senza parametro displayOnly, default e' true
            var displayOnly = (displayOnly===false) ? false : true;

            // Se viene passato un nome di classe, verra' usato al posto del default.
            var cls = (cls) ? cls : 'x-item-disabled';
            
            // Aggiunge o rimuove la classe
            if (displayOnly) {
                this.el.addClass(cls);
            } else {
                this.el.removeClass(cls);
            }        

            // Setta a readOnly il sottostante elemento DOM
            this.el.dom.readOnly = displayOnly;

            var xType = this.getXType();
            
            if (xType=='checkbox'){
            	// E' necessario configurare l'attributo readOnly per le checkbox
            	if (displayOnly)
            		this.disable();
            	else
            		this.enable();
            }

            // Caso gruppi radio o checkbox
            if (xType == 'radiogroup' || xType == 'checkboxgroup') {
                var items = this.items.items;
                for (var i=0; i<items.length; i++) {
                	if (displayOnly)
                		items[i].disable();
                	else
                		items[i].enable();
                };            
            }

            if (xType=='htmleditor'){
            	if (displayOnly) {
            		this.syncValue();
            		var roMask = this.wrap.mask();
            		roMask.dom.style.filter = "alpha(opacity=100);"; //IE
            		roMask.dom.style.opacity = "1"; //Mozilla        			
            		roMask.dom.style.background = "white";
            		roMask.dom.style.overflow = "scroll";
            		roMask.dom.style.font = "normal 12px tahoma, arial, helvetica, sans-serif";
            		roMask.dom.style.color = "gray";
            		roMask.dom.style.zIndex = "0";
            		roMask.dom.innerHTML = this.getValue();
            		
            		/* QUESTA SOLUZIONE ALTERNATIVA E PIU' PULITA
            		 * FUNZIONA CON FIREFOX MA NON CON IE
            		this.getToolbar().hide();
            		this.execCmd("contentReadOnly", true);
            		*/
            	}
            	else {
            		if(this.rendered){
            			this.wrap.unmask();
            		}
            		this.getToolbar().show();
            	}
            }

            // I campi trigger (es. date,time,dateTime) vengono disabilitati. 
            // Add eccezione di quelli con triggerClass = 'x-form-link-trigger'.
            if (this.trigger) {
            	if (displayOnly && this.triggerClass != 'x-form-link-trigger')
            		this.disable();
            	else if (!displayOnly)
            		this.enable();
            }
        },
        isReadOnly: function(){
            if (this.disabled )
                return true;
            else
                if ( this.el.hasClass('x-item-disabled') ){
                    return true;
                }

            return false;
        }
});

/* * Al: Adaptive Library
 */
Al = function(){
    // inizializzazione
    // alert("qui ci passo quando viene caricata la pagina");
    var v = "0.1"; 
    // metodi esposti
    return {
        version : this.v,
        usage : function(){
            alert(" property: methods:");
        },
        /*
         * Get element by id shortHand
         */
        $ : function(id){
            return document.getElementById(id);
        },
        /*
         * Include di un file js tramite URL
         */
        loadJS : function(urlScript) {
              var oNewScript = document.createElement("script");
              oNewScript .type = "text/javascript";
              oNewScript .src = urlScript;
              
              document.getElementsByTagName("head")[0].appendChild(oNewScript );
        },    
        /*
         * Metodo di iterazione con callaback sui fields
         */ 
        each : function(tag, callback){
            var elementList = this.$(tag);
            for (var el in elementList){
                callback(el);
            }
        },
        /*
         * Creazione finestra modale
         */ 
        createDiv : function(id, html, width, height, left, top) {

           var newdiv = document.createElement('div');
           newdiv.setAttribute('id', id);
           newdiv.setAttribute('z-index','10');
           
           if (width) {
               newdiv.style.width = 300;
           }
           
           if (height) {
               newdiv.style.height = 300;
           }
           
           if ((left || top) || (left && top)) {
               newdiv.style.position = "absolute";
               
               if (left) {
                   newdiv.style.left = left;
               }
               
               if (top) {
                   newdiv.style.top = top;
               }
           }
           
           newdiv.style.background = "#00C";
           newdiv.style.border = "4px solid #000";
           
           if (html) {
               newdiv.innerHTML = html;
           } else {
               newdiv.innerHTML = "nothing";
           }
           document.body.appendChild(newdiv);
        },
        /*
         * Simulazione ritardo d'esecuzione
         * @param delay secondi
         */       
        sleep : function(delay) {
            var delay = delay * 1000;
            var start = new Date().getTime();
            while (new Date().getTime() < start + delay);
        },  
        equalsIgnoreCase : function(str,arg)
        {               
                return (new String(str.toLowerCase())==(new
                                    String(arg)).toLowerCase());
        },
        wordWrap : function(str, len, b, c){
            var i, j, s, r = str.split("\n");
            if(len > 0) for(i in r){
                for(s = r[i], r[i] = ""; s.length > len;
                    j = c ? len : (j = s.substr(0, len).match(/\S*$/)).input.length - j[0].length
                    || len,
                    r[i] += s.substr(0, j) + ((s = s.substr(j)).length ? b : "")
                );
                r[i] += s;
            }
            return r.join("\n");
        },
        addHidden : function addInput(divName, fieldName, value){
            var newdiv = document.createElement('div');
            newdiv.innerHTML = "<input type='hidden' name='"+fieldName+"' value='"+value+"'>";
            document.getElementById(divName).appendChild(newdiv);
        }
    };
}();


 
/* 
 * TODO 
 * 
Al.Template = function() {
	
	html=this.time+':['+this.string+']<br/>';
	return html;
	
	this.
}
*/


/*
 * ExtJS Dipendent Code
 */
if (Ext != undefined){
    Al.x = Ext;
    /*
     * Scandisce un formPanel e restituisce i campi e i valori
     * in un elenco separato da virgole
     */
    Al.Form = function(IDformPanel){
        this.fieldList = "";
        this.valueList = ""
        this.form_fields = Ext.getCmp(IDformPanel).getForm().getValues();
        
        // Ciclo acquisizione campi
        for (var field_name in this.form_fields){
            this.fieldList += field_name.replace("_search","").replace("_ref","") + ",";
            this.valueList += this.form_fields[field_name] + ",";
        }
        // Rimuvo l'ultima virghiula
        this.valueList = this.valueList.replace(/,$/,"");
        this.fieldList = this.fieldList.replace(/,$/,"");
        
        // Metodo per pulizia dei campi
        this.clearFields = function(){
            for (var field_name in this.form_fields){
                Ext.getCmp(field_name).setValue("");
            }
            return this;
        }
        // Metodo di iterazione con callaback sui fields
        this.each = function(callback){
            for (var f in this.form_fields){
                callback(f);
            }
        }

    }

    /**
     * Intercetta il messaggio d'errore da una chiamata in formato JSON
     */
    Al.InterceptError = function(conf){
        var reader = conf.reader || {};
        var askMsg = conf.askMsg || {};
        try{
            
            if (reader.jsonData.topics[0] != undefined){
                if (reader.jsonData.topics[0].session != undefined){
                    Ext.MessageBox.show({
                       title: reader.jsonData.topics[0].session,
                       msg: askMsg,
                       buttons: Ext.MessageBox.YESNO,
                       fn: function(btn){ 
                                if (btn == "yes") 
                                    window.location.reload(); 
                                runCallback();
                       },
                       animEl: 'mb4',
                       icon: Ext.MessageBox.WARNING
                    });
                }else if(reader.jsonData.topics[0].error) {
                    Ext.MessageBox.show({
                       title: reader.jsonData.topics[0].error,
                       msg: reader.jsonData.topics[1].details,
                       buttons: Ext.MessageBox.OK,
                       fn: function(btn){ runCallback(); },
                       animEl: 'mb4',
                       icon: Ext.MessageBox.WARNING
                    });
                }
                function runCallback(){
                    try{
                        if (typeof reader.jsonData.topics[2].callback !== undefined){
                            var functionCallback = eval(reader.jsonData.topics[2].callback); 
                        }
                    }catch(e){
                        debug("Al.InterceptError exception: " + e);
                    }
                }
            }
        }catch ( e ){
            alert("Error: " + e);
        }
    }
}



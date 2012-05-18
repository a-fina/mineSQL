/**
 * Centralizza le dichiarazioni dei filtri sulle grid.
 * A seconda del idFlusso attiva o meno determinati filtri sulle colonne.
 */

/*
 * Gestore di un signolo filtro in sessione
 */
FlussoFilter = function(){
   this.filters;
   
   // Riceve un compenente filtro e lo se lo salva in sessione
   this.saveFilter = function(ExtFilter){
        //FW300609 alert("MARK 13 FlussoFilter.saveFilter: " + ExtFilter.dataIndex + " values: " +  ExtFilter.getValue());
        this.filters = ExtFilter.getValue();
   }
   // Riceve un compenente filtro e lo setta come in sessione
   this.setFilter = function(ExtFilter){
        if ( typeof this.filters != 'undefined' ){
            ExtFilter.setValue(this.filters);
            //alert("MARK 10.1 FlussoFilter.setFilter: " + ExtFilter.dataIndex + " values:" + ExtFilter.getValue() + " id:" + ExtFilter.id);
            ExtFilter.setActive(true);
            //FW300609 
        }
   }
   // Stampa di debug
   this.printFilter = function(){
        debug("FlussoFilter statoFilter: " + this.filters);
   }
}

/*
 * Metodo che viene richiamato da ogni griglia flusso
 */
function defineColumnFilters(idFlusso)
{ 
    if ( typeof(SessionFilters) == "undefined"){
        SessionFilters = {};
    }

    //alert(" MARK entry defineColumnFilters");
    // Componente che contiene tutti i filtri delle colonne di una griglia 
    var flussoFilters = new Ext.grid.GridFilters({
        filters:[]
    });
    // A seconda del flusso decido quali colonne filtrare
    //TODO Creare i filtri lato server in auto-json.jsp
	/******* if (idFlusso == ID_FLUSSO_COMPENDING) {
        elencoFiltri = [{dataIndex:"IDCOMPONENTE", type:"numeric"},
                        {dataIndex:"CODICE_COMPONENTE", type:"string"},
                        {dataIndex:"DATA_CREAZIONE", type:"string"},
                        {type: 'list', dataIndex: 'TIPO_COMPONENTE', url: 'combos/combos.jsp?table=TIPO_COMPONENTE'},
                        {dataIndex:"PRODOTTO_COMPONENTE", type:"string"},
                        {type: 'list', dataIndex: 'STATO_COMPONENTE', url: 'combos/combos.jsp?table=STATO_COMPONENTE'},
                        {dataIndex:"ORDINE", type:"string"},
                        {type: 'list', dataIndex: 'STATO_ORDINE', url: 'combos/combos.jsp?table=STATO_ORDINE'},
                        {dataIndex:"ROWID_RO", type:"string"},
                        {dataIndex:"IDDEFECT", type:"string"},
                        {dataIndex:"BLOCCATO", type:"string"},
                        {type: 'list', dataIndex: 'TIPO_LAVORAZIONE', url: 'combos/combos.jsp?table=TIPO_LAVORAZIONE'},
                        {type: 'list', dataIndex: 'AZIONE', url: 'combos/combos.jsp?table=AZIONE'},
                        {type: 'list', dataIndex: 'FASE', url: 'combos/combos.jsp?table=FASE'},
                        {type: 'list', dataIndex: 'STATO_RIGA_ORDINE', url: 'combos/combos.jsp?table=STATO_RIGA_ORDINE'}
        ];
	}else*/ if (typeof idFlusso == "object"){
        elencoFiltri = idFlusso.concat( [{dataIndex:"IDCOMPONENTE", type:"string"},
                        {dataIndex:"TITOLO", type:"string"},
                        {dataIndex:"PRP_ID", type:"string"},
                        {type: 'list', dataIndex: 'URGENZA', url: 'combos/combos.jsp?table=urgenza'},
                        {type: 'list', dataIndex: 'SISTEMA', url: 'combos/combos.jsp?table=sistema'},
                        {type: 'list', dataIndex: 'IMPATTO', url: 'combos/combos.jsp?table=impatto'},
                        {type: 'string',  dataIndex: 'IDWORKAROUND'},
                        {type: 'string',  dataIndex: 'NOME', header: 'TITOLO'},
                        {type: 'list', dataIndex: 'SISTEMA', url: 'combos/combos.jsp?table=sistema'},
                        {type: 'string', dataIndex: 'IDDEFECT'},
                        {type: 'string', dataIndex: 'NOME', header: 'TITOLO'},
                        {type: 'list', dataIndex: 'PRIORITA', url: 'combos/combos.jsp?table=priorita'},
                        {type: 'list', dataIndex: 'URGENTE', url: 'combos/combos.jsp?table=urgenza'},
                        {type: 'string',  dataIndex: 'IDDEFECT'},
                        {type: 'string',  dataIndex: 'IDTASK'},
                        {type: 'string',  dataIndex: 'IDBUGQC'}, 
                        {type: 'list', dataIndex: 'IMPATTO', url: 'combos/combos.jsp?table=impatto'},
                        {type: 'list', dataIndex: 'URGENZA', url: 'combos/combos.jsp?table=urgenza'},
                        {type: 'list', dataIndex: 'IDSTATOQC', url: 'combos/combos.jsp?table=statoqc'},
                        {type: 'list', dataIndex: 'STATOQC', url: 'combos/combos.jsp?table=statoqc'}
                    ]);
        idFlusso = "prova_report";
        SessionFilters[idFlusso] = {};
    }

    // Il filtro sul gruppo dipende dal ruolo dell'utente, se readOnly niente filtro
    /**if ( idFlusso == ID_FLUSSO_COMPENDING && ! isLimitedUser() ){
        elencoFiltri.push( {type: 'list', dataIndex: 'GRUPPO', url: 'combos/combos.jsp?table=gruppo'});
    } **/
    
    // Costruisco il filtro sulla griglia
    for (i=0;i<elencoFiltri.length;i++){
        var nome = elencoFiltri[i].dataIndex;
        // Istanzio un componente filtro a seconda del tipo
        switch(elencoFiltri[i].type){
            case "string":
                var currentFilter = new Ext.grid.filter.StringFilter({
                        type: 'string',  dataIndex: nome
                });
                break;
            case "numeric":
                var currentFilter = new Ext.grid.filter.StringFilter({
                        type: 'numeric',  dataIndex: nome
                });
                break;
            case "date":
                var currentFilter = new Ext.grid.filter.StringFilter({
                        type: 'date',  dataIndex: nome
                });
                break;
            case "list": 
                var currentFilter = new Ext.grid.filter.ListFilter({
                    dataIndex: nome,
                    id: 'NOME',
                    labelField: 'NOME', 
                    store: new Ext.data.JsonStore({
                                url: elencoFiltri[i].url,
                                root: 'topics',
                                id: 'NOME',
                                fields: [ 'NOME' ]
                            }),
                    loadOnShow: true
                });
                break;
        }

        try
        {
            // Inizializzo il filtro con il valore che ho nella sessione
            if ( typeof(SessionFilters[idFlusso][nome]) != "undefined"){
                if ( typeof(SessionFilters[idFlusso][nome].setFilter ) == "undefined"){
                    //FW300609 alert(" attacco setFilter " );
                    var proto = new FlussoFilter;
                    SessionFilters[idFlusso][nome].setFilter = proto.setFilter;
                }
                //FW300609 alert(" 9 Imposto il filtro che ho in sessione  nome: " + nome + " con: " + currentFilter );
                SessionFilters[idFlusso][nome].setFilter(currentFilter);
            }
        }
        catch(e){
            alert(" GridAdvancedFilters.js exception: " + e);
        }
        //DEBUG currentFilter.on("update", function(){ alert(" Update padre filtro idx: " + this.dataIndex); });
        
        // Lo appiccio al GridFiltro
        try{
            flussoFilters.addFilter(currentFilter);
        }catch(e){
            alert("addFilter ex:" + e);
        }
    }

            //alert(" MARK ENTER update filters cicle ");
    // Salvo o rimuovo in sessione il filtro clickato 
	flussoFilters.filters.each(function(filter) {
            // Intercetto il click sul sul filtro 
            filter.on("update", function(){ 
                //FW300609  
                if (this.getValue() != ""){ 
                    // Salvo lo stato 
                    //alert("onUpdate filtro: " + filter.dataIndex + " vale: " + this.getValue() );
                    SessionFilters[idFlusso][filter.dataIndex] = new FlussoFilter();
                    SessionFilters[idFlusso][filter.dataIndex].saveFilter(this); 
                }else{
                    // Resetto alert("Resetto filtro:" + filter.dataIndex );
                    delete SessionFilters[idFlusso][filter.dataIndex];
                }
            });
    });

    return flussoFilters;
}

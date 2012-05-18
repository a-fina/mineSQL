    function getHiddenColumns(columnModel){
      var tempStr ="";
      Ext.each(columnModel.config, function(col, idx) {
          if (columnModel.isHidden(idx)){
            tempStr += columnModel.getColumnHeader(idx)+","; 
          }
      });
      return tempStr.replace(/,$/,"");
    }

/**
 * ToolBar delle griglie.
 * Lo stato del filtro per gruppo/lavorazione é condiviso tra tutte le griglie.
 * Il campo di ricerca invece é unico per ogni griglia.
 *
 * @param property Object
 * @return items Array
 */
GridToolBar =  function(property){
    // Private variable
    var _idFlusso = property.idflusso || undefined;
    var _columnModel = property.column || undefined;
    var _entity = property.entityName || undefined;
    var _idQuery = property.idquery || undefined;
    var _cm = property.column; 
    var _idFiltro = property.idFiltro || undefined;

    // Alcuni componenti del menu sono statici e globali
    // TODO: il menu filter non server perché c'é il nuovo
    // menu "Load" che carica gli script e la form di interfaccia necessaria
    // il menu filtri conteneva delle condizioni preconfigurate (Task in lavorazione etc.), 
    // quindi per avere lo stesso effetto basta creare un nuovo script che ha come campi
    // le varibili della query che si vogliono parametrizzare
    //  var _filterMenu = property.filterMenu || filterMenu;
    //  var _searchMenu = property.searchMenu || searchMenu;
    var _tableName = property.showTableName || "DM_QUERY_T";
    var _databaseName= property.databaseName || "DM_QUERY_T";
    var _hostName = property.hostName || "DM_QUERY_T";

    // Per ora sta roba server ad InteractiveSQL
    var _bodyQuery = property.bodyQuery || undefined;
    var _target    = property.target || undefined;

    var uniqId = "id-" + _hostName + _databaseName + _tableName + _idFiltro;
    // Public items
    this.exportExcelForm = 
                "<form name='exportReport' id='exportReport' action='autogrid/auto-excel.jsp' method='post'>"+
                "<div id='" + uniqId + "'>"+
                "<input type='hidden' name='hidden_columns' value=''>"+
                /*"<input type='hidden' name='idQuery' value=''>"+
                "<input type='hidden' name='tableName' value='"+_tableName +"'>"+
                "<input type='hidden' name='context' value='" + _entity + "'>"+*/
                "</div>"+
                "</form>";

    // Campo di ricerca
    this.ricercaField = property.ricercaField || undefined;
    // Bottone di salvataggio dei filtri
    this.saveReportButton = {
              text:'Salva Report',
              iconCls:'icon-save',
              handler: function(){ 
                        var hidden = getHiddenColumns(_cm); 
                        // Apro la finestra per l'inserimento di un nuovo filtro
                        // TODO Togliere da qui e forwardare ajaxParams dei chiamanti
                        var ajaxParams = {
                                "idQuery":_idQuery,
                                "context":_entity,
                                "IDFLUSSO": _idFlusso,
                                "hidden_columns": hidden
                        };
                        Ext.apply(ajaxParams, property);
                        
                        if ( _idFlusso == ID_FLUSSO_GENERIC ) {
                                    ajaxParams.saveSessionFilter = "false"; // TODO serve ? se questo é true vengono sovrascitti i filtri in sessione
                        }
                        /*********
                        if ( typeof (_bodyQuery ) !== "undefined" ){
                            ajaxParams.bodyQuery = _bodyQuery;
                            ajaxParams.target = _target;
                        }
                        *******/
                        alert("ajaxParams FilterMngr.saveWin");
                        console.dir(ajaxParams);

                        var saveFilterWin = new FilterMngr.saveWin({
                                    submitParams: ajaxParams
                        });
                        saveFilterWin.show(this);
                }
    };

    //TODO abilitare solo questo non saveReport
    this.updateFilterButton = {
             text:'Salva',
             iconCls:'icon-update',
             handler: function(){
                        // Fotografo i filtri attulamente attivi sul flusso corrente
                        // Rimuovo i filtri globali
                        var filter2string = Ext.util.JSON.encode(SessionFilters[_idFlusso]);
                        
                        var hidden = getHiddenColumns(_cm); 
                        // Apro la finestra per l'inserimento di un nuovo filtro
                        // TODO Togliere da qui e forwardare ajaxParams dei chiamanti
                        var ajaxParams = {
                                "ID": _idFiltro,
                                "IDFLUSSO": _idFlusso,
                                "context":_entity,
                                "hidden_columns": hidden,
                                "bodyQuery": filter2string 
                        };
                        if ( _idFlusso == ID_FLUSSO_GENERIC ) { //TODO serve ?
                                    ajaxParams.saveSessionFilter = "false"; // se questo é true vengono sovrascitti i filtri in sessione
                        }
                        ajaxParams.target = _target; //TODO serve ?
                        Ext.apply(ajaxParams, property);
                        var updateFilterWin = new FilterMngr.updateWin({
                                    submitParams: ajaxParams
                        });
                        updateFilterWin.show(this);
             }
    };

    this.saveFilterButton = {
             text:'Save New...',
             iconCls:'icon-save',
             handler: function(){
                        // Fotografo i filtri attulamente attivi sul flusso corrente
                        // Rimuovo i filtri globali
                        var filter2string = Ext.util.JSON.encode(SessionFilters[_idFlusso]);
                        
                        var hidden = getHiddenColumns(_cm); 
                        // Apro la finestra per l'inserimento di un nuovo filtro
                        // TODO Togliere da qui e forwardare ajaxParams dei chiamanti
                        var ajaxParams = {
                                //"idQuery":_idQuery,
                                "context":_entity,
                                "hidden_columns": hidden,
                                "IDFLUSSO": _idFlusso,
                                "bodyQuery": filter2string 
                        };
                        if ( _idFlusso == ID_FLUSSO_GENERIC ) { //TODO serve ?
                                    ajaxParams.saveSessionFilter = "false"; // se questo é true vengono sovrascitti i filtri in sessione
                        }
                        ajaxParams.target = _target; //TODO serve ?
                        Ext.apply(ajaxParams, property);
                   /*******     ajaxParams.databaseName = 'mineSQL';
                        ajaxParams.tableName = 'msq_FILTRI_T';
                        ajaxParams.hostName = 'localhost'; *******/

                        var saveFilterWin = new FilterMngr.saveWin({
                                    submitParams: ajaxParams
                        });
                        saveFilterWin.show(this);
             }
    };
    
    this.filterTool = new Ext.menu.Menu();

    // Costruzione dell'array degli elementi del toolBar
    var _items = [];

    // MEGA IF
   /******
   if (_idFlusso != ID_FLUSSO_GENERIC){
       **********/
        // Menu Filter
        /** TODO: non servono piu: per le ricerche usare le colonne ExtJs, per i filtri usare menu "Load script" 
        _items.push({
            text:'Filtra per: ',
            iconCls: 'filter',
            menu: _filterMenu
        }); 
        _items.push('-');

        // Search Button
        _items.push({
                    text:'Ricerca: ',
                    tooltip: 'Caratteri jolly: \'_\' sostitutivo di un singolo carattere, \'%\' sostitutivo di un blocco di caratteri',
                    iconCls: 'icon-find',
                    menu: _searchMenu
                });
        _items.push('<div id="filterlabel" >'+ getCurrentSearch(_idFlusso) +'</div>'); // load session value
        _items.push(this.ricercaField); 
        **********/
        _items.push('-');

        // Aggiungo ulteriori items passati in input
        if ( typeof(property.items) == "undefined"){
            property.items = [];
        }
        for (i=0; i<property.items.length; i++){
            _items.push(property.items[i]);
        }


        // Export Excel Button and Form
        _items.push('->');
        _items.push(this.exportExcelForm);
        _items.push({
                  text:'Export to Excel',
                  iconCls:'icon-excel',
                  handler: function(){ 
                        for (var p in property){
                             Al.addHidden(uniqId , p, property[p]);
                        }
                        var  hidden = getHiddenColumns(_cm); 
                        document.exportReport.hidden_columns.value = hidden;
                        //document.exportReport.idQuery.value = _idQuery;
                        document.exportReport.submit();
                      }
                 });
        // Aggiungo il bottone update solo quando sto caricando una griglia 
        // fitrata
        if (typeof(_idFiltro) !== "undefined" ) 
             this.filterTool.add(this.updateFilterButton); 
        // Questa c'é sempre
        this.filterTool.add(this.saveFilterButton);

        /***** TODO Non funziona
        this.filterTool.add({
                text: "Reset",
                iconCls:'icon-refresh',
                handler: function(){
                    // Resetto i filtri della griglia
                    var gridName =  _entity.toLowerCase() + "Grid";
                    $$(gridName).filters.clearFilters();
                    SessionFilters[_idFlusso] = {};
                }
        }); ******/

        // TODO: abilitare solo dopo che é stato premuto il bottone "Run query" altrimenti
        // non salva un cazz
        _items.push({
                text: 'Query Menu',
                iconCls: 'filter-menu',
                menu: this.filterTool 
        });

		_items.push({
		    text:'Update from CSV',
		    iconCls: 'icon-excel',
		    handler: function(){ 
                var ajaxParams = {}
                Ext.apply(ajaxParams, property);
                uploadFile(property); 
            }
		});

    /***
    }else{
        // Save Filter Buttton
        _items.push(this.saveReportButton);
    }
    ********/

    // return array items   
    return _items; 
}

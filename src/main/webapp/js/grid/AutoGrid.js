// vim: ts=4:sw=4:nu:fdc=4:nospell
/*
 * @class Autogrid
 * @param targetDiv
 * @param targetURL
 * @param ajaxParams Object 
 * @param config Object
 *
 * @return Object
 *
 * Griglia generica: le colonne e le righe sono istanziate lato
 * server e adattabili a qualsiasi tabella.
 *
 * @author    Finamore Alessio
 * @date      27 / 05 / 10
 * @version   0.1
 * @revision  $Id: 
 *
 * @license licensed under the terms of
 * the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
 * that the code/component(s) do NOT become part of another Open Source or 
 * Commercially licensed development library or toolkit 
 * without explicit permission.
 * 
 * <p>License details: <a href="http://www.gnu.org/licenses/lgpl.html"
 * target="_blank">http://www.gnu.org/licenses/lgpl.html</a></p>
 *
 *
 */

//startPage = 0;
AutoGrid = function (/*targetDiv,*/ targetURL, ajaxParams, config) {

    //this.targetDiv = targetDiv;
    this.targetURL = targetURL;
    this.ajaxParams = ajaxParams;
    this.config = config;

    this.cm = {};
    this.ds = {};

    //Ext.get(this.targetDiv).update('<br/><br/><center><img src="../img/bigloading.gif"/><h2>Loading ...</h2></center>');
    /**
     * Aggiunge elementi ad una form
     */
    function formAdd(type, name, value) {
        //Create an input type dynamically.
        var element = document.createElement("input");

        //Assign different attributes to the element.
        element.setAttribute("type", type);
        element.setAttribute("value", value);
        element.setAttribute("name", name);

        var foo = document.getElementById("exportReport");

        //Append the element in page (in span).
        foo.appendChild(element);
        //alert(" add "+ type+ " name:" + name + " val: "+value); 
    }
    /**
     * Scandisce il json e definisce i filtri su tutte le colonne
     */
    function getColumnFilter(column) {
        var elencoFiltri = [];
        for (var i = 0; i < column.length; i++) {
            var colName = column[i].dataIndex;
            var o = {type: 'string', dataIndex: colName};
            elencoFiltri.push(o);
        }
        return defineColumnFilters(elencoFiltri);
    }

    // row expander
    var expander = new Ext.grid.RowExpander({
        tpl: new Ext.Template(
                '<br/>',
                '<p><b>Descrizione:</b> {DESCRIZIONE}</p>'
                )
    });

    // Valuta la risposta ajax e disegna la griglia nel div
    function getGridPanel(response, /*_div,*/ _url, params) {
        this.params = params || {};
        // Intercetto e valuto la risposta JSON
        var json = eval("(" + response.responseText + ")");

        var pagingBar = undefined;
        // Intercetto errori
        if (typeof json.error != "undefined") {
            Ext.MessageBox.show({
                title: 'Attenzione',
                msg: json.error,
                buttons: Ext.MessageBox.OK,
                //fn: function(btn){ alert("bho: " + btn); },
                animEl: 'mb4',
                icon: Ext.MessageBox.WARNING
            });
            return;
        }
        // Intestazione della griglia
        var colMod = new Ext.grid.ColumnModel(json.column);
        // Scandisco le colonne e ci attacco il filtro di ricerca 
        var columnFilter = getColumnFilter(json.column);

        // Prendo le righe della griglia: o mi arrivano tutte insieme dal JSON
        // o uso un mio dataStore con paginazione
        var dataStore = {};
        if (typeof json.data != "undefined") {
            dataStore = new Ext.data.SimpleStore({
                fields: json.fields,
                data: json.data
            });
        } else {
           // alert("MARK_AP_REPORT_OUT_T");
            var myParams = {
                limit: 20, 
                dir: 'ASC', 
                data: true, 
                meta: true, 
                act: 'AP_REPORT_OUT_T'
            };
            Ext.apply(myParams, this.params);
            dataStore = new Ext.data.JsonStore({
                url: _url,
                root: json.root,
                fields: json.fields,
                baseParams: myParams
            });
            pagingBar = new Ext.PagingToolbar({
                pageSize: 20,
                store: dataStore,
                displayInfo: true,
                displayMsg: 'Displaying topics {0} - {1} of {2}',
                emptyMsg: "No topics to display"
            });

        }
        colMod.defaultSortable = true;

        //alert("dataStore AutoGrid.js");
        var toolBarParam = {
            column: colMod,
            dataStore: dataStore,
            entityName: 'Report'
        };
        Ext.apply(toolBarParam, ajaxParams);
        // Prendo il bottone Salva Filter
        var toolBar = new GridToolBar(
                //idflusso:ID_FLUSSO_GENERIC, 
                toolBarParam
                );
        /*
         * Metodo di disegno della griglia
         */
        var renderGrid = function () {

            // GRiglia principale
            var autoGrid = new Ext.grid.GridPanel({
                //renderTo:_div, //this.targetDiv,
                plugins: [columnFilter, expander],
                layout: 'fit',
                //autoHeight: true,
                //width: 500,
                //autoWidth: true,
                autoScroll: true,
                //height: 320,
                title: this.params.title,
                store: dataStore, //this.ds,
                cm: colMod, //this.cm,
                trackMouseOver: true,
                sm: new Ext.grid.RowSelectionModel({selectRow: Ext.emptyFn}),
                loadMask: true,
                viewConfig: {
                    forceFit: true
                },
                tbar: [{
                        text: 'Print',
                        iconCls: 'icon-print',
                        handler: function () {
                            window.print();
                        }
                    },
                    /*    
                     "<form name='exportReportCSV' id='exportReportCSV' action='autogrid/auto-excel.jsp' method='post'>" +
                     "<div id='nomeCasuale'></div>" +
                     "<input type='hidden' name='idQuery' value=''>"+
                     "<input type='hidden' name='hidden_columns' value=''>"+
                     "<input type='hidden' name='tableName' value='msq_SCRIPT_T'>"+
                     "<input type='hidden' name='context' value='Report'>"+
                     "</form>"
                     */
                    /*,{
                     text:'Export to CSV',
                     iconCls:'icon-excel',
                     handler: function(){ 
                     var  hidden = getHiddenColumns(colMod); 
                     try{
                     document.exportReport.hidden_columns = {};
                     }catch(e){
                     alert(" Autogrid exception  ");
                     }
                     //document.exportReport.hidden_columns.value = hidden;
                     // Forward parameters, lo aggiungo o lo sovrascrivo
                     for (var p in ajaxParams){
                     alert(" addHidden " + p+": "+ajaxParams[p]);
                     // if ( typeof(document.exportReport[p]) === "undefined")
                     Al.addHidden("nomeCasuale" , p, ajaxParams[p]);
                     // else
                     //    document.exportReport[p].value = ajaxParams[p];
                     }
                     document.exportReport.submit();
                     console.dir(document.exportReport);
                     }
                     }*/
                    "->"
                            , toolBar
                ],
                bbar: pagingBar
            });
            // trigger the data store load

            //dataStore.load({params:{start:0, limit:20, dir:'ASC', data: true, meta: true}}); //{params:myParams});
            dataStore.load({params: {start: 0}});
            // render it
            //TODO autoGrid.render();

            /*
             ricercaField.setValue(SessionFilters[ID_FLUSSO_PROBLEM][currentQuery]);
             function toggleDetails(btn, pressed){
             var view = grid.getView();
             view.showPreview = pressed;
             view.refresh();
             } */

            return autoGrid;
        };

        // Assembla il gridPanel
        return renderGrid(ajaxParams);//this.ds,this.cm,this.targetDiv);
    }
    /*
     * Metodo di caricamento metaDati e Dati
     */
    this.getJSONGrid = function (mainPanel) {
        //alert(" Autogrid->this.getJSONGrid");
        var _url = this.targetURL;
        //var _div = this.targetDiv;
        var _params = this.ajaxParams;

        var myParams = {column: true, meta: true};
        Ext.apply(myParams, this.ajaxParams);

        //Ext.get(this.targetDiv).update( '<br/><br/><center><img src="../img/bigloading.gif"/><h2>Loading ...</h2></center>');
        mainPanel.items.each(function (c) {
            mainPanel.remove(c);
        })

        var waitingPanel = new Ext.Panel({
            region: 'center',
            html: '<br/><br/><center><img src="http://' + HOST + '/img/bigloading.gif"/><h2>Loading ...</h2></center>'
        });
        mainPanel.add(waitingPanel);
        mainPanel.doLayout();
    
      //  alert("MARK_ before ajax request in AutoGrid.js");
        Ext.Ajax.request({
            url: this.targetURL,
            success: function (response) {
                //Ext.get(_div).update('');
                mainPanel.remove(waitingPanel, true);
                mainPanel.add(getGridPanel(response, /*_div,*/ _url, _params));
                mainPanel.doLayout();
            },
            failure: function () {
                mainPanel.remove(waitingPanel, true);
                mainPanel.doLayout();
                Ext.MessageBox.alert("Errore di caricamento");
            },
            params: myParams
        });
    };
};

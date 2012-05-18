// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
 * @class iSQL
 * @config Object: sono i parametri di configurazione passati
 *                 passati a WindowFormTable
 * @return Object
 *
 * Genera un item del menu che apre una finestra dove si puo eseguire
 * codice SQL interattivamente o con una form con dei campi predefiniti
 * caricati lato server che servono all'esecuizione dello script PL
 *
 * @author    Finamore Alessio
 * @copyright (c) 2010, by Assioma.net
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
iSQL = function(config){

    var ver = "0.9";
    var contatoreWindows = 0;

    var interactiveSQL = undefined;

    /**
     * Rchiama la pagina che costruisce lato server tutto il JSON che serve
     * per la costruzione della griglia
     */
    function loadAutoGrid(params){
        var url = params.url || "http://"+HOST+"/autogrid/auto-json.jsp";
        var div = params.target || "principale";
        var config = {};

        var myGrid = new AutoGrid(div, url, params, config); 
        myGrid.getJSONGrid();
    }

    var openNewWindow = function(){
            //Ext.get('principale').update('');
            contatoreWindows++;

            var queryBodyID = 'queryBody'+contatoreWindows;
            var queryPanelID = 'queryPanel'+contatoreWindows;

            //alert("contatoreWindows:" + contatoreWindows) 

            function runQuery(){ 
                var query = $$(queryBodyID).getValue();
                loadAutoGrid({  
                        action: 'run', 
                        bodyQuery: query, 
                        target: queryPanelID
                }); 
            }

            var sqlForm = new Ext.form.FormPanel({
                baseCls: 'x-plain',
                //id: 'queryForm',
                labelWidth: 55,
                url:'save-form.php',
                autoScroll: true,
                defaultType: 'textfield',
                tbar: [{ 
                        text: "Run",
                        handler: runQuery
                        },{
                        text: "Clear",
                        handler: function(){$$(queryBodyID).reset()} 
                }],
                items: [{
                   xtype: 'textarea',
                   region: 'north',
                   hideLabel: true,
                   id:queryBodyID,
                   collapsible:true,
                   name: 'queryBody',
                   //anchor: '98%',  // anchor width by percentage and height by raw adjustment
                   height: 100
                /**},{
                   xtype: 'panel',
                   id:queryPanelID,
                   region: 'center',
                   name: 'queryPanel',
                   layout:'fit',
                   anchor: '100%'  // anchor width by percentage and height by raw adjustment
                   //xtype: 'combobox',
                   //id: 'queryHistory', */
                },{
                    "xtype":"combo",
                    "id":"yySUBMIT_filtro",
                    "tpl":tplCombo,
                    "store":new Ext.data.JsonStore({ 
                        url:'combos/combos.jsp?table=cp_gruppo_t', 
                        root: 'topics', 
                        fields: [ 'ID', 'NOME' ]
                    }),
                    "displayField":"NOME",
                    "valueField":"ID",
                    "disabled":false,
                    "editable": false,
                    "typeAhead": true,
                    "fieldLabel":"filtro1",
                    "name":"SUBMIT_filtro",
                    "triggerAction": 'all',
                    "emptyText":"Scegli...",
                    "hiddenName":"SUBMIT_filtro",
                    "selectOnFocus":true,
                    "anchor":"100%",
                    "allowBlank":false
                    },{
                    "xtype":"combo",
                    "id":"xxSUBMIT_filtro",
                    "tpl":tplCombo,
                    "store":new Ext.data.JsonStore({ 
                        url:'combos/combos.jsp?table=cp_gruppo_t', 
                        root: 'topics', 
                        fields: [ 'ID', 'NOME' ]
                    }),
                        "displayField":"NOME",
                        "valueField":"ID",
                        "disabled":false,
                        "editable": false,
                        "typeAhead": true,
                        "fieldLabel":"filtro2",
                        "name":"xxSUBMIT_filtro",
                    "triggerAction": 'all',
                    "emptyText":"Scegli...",
                    "hiddenName":"SUBMIT_filtro",
                    "selectOnFocus":true,
                    "anchor":"100%",
                    "allowBlank":false
                    }//)
                    ]
            });

            var sqlWin = new Ext.Window({
                title:'Ricerca avanzata',
                closable:true,
                width:600,
                height:450,
                modal: false,
                resizable: false,
                border:true,
                collapsible: true,
                plain:true,
     //           layout: 'fit',
                closeAction: 'close',
                constrain: true,
                buttonAlign:'center',
                items: [sqlForm,
                {
                   xtype: 'panel',
                   id:queryPanelID,
                   region: 'center',
                   name: 'queryPanel',
                   layout:'fit',
                   anchor: '100%'  // anchor width by percentage and height by raw adjustment
                   //xtype: 'combobox',
                   //id: 'queryHistory',
                }]
            });
            sqlWin.show();
            /*** NON VA Create a KeyMap
            var map = new Ext.KeyMap($('queryBody'), {
                key: Ext.EventObject.F9,
                //ctrl: true,
                fn: runQuery,
                scope: this
            });
            ***/
            
        }
    return {
        version : function(){
           alert("Version: " + ver);
        },
        openWindow : function(){
            openNewWindow();
        }
    };
}();

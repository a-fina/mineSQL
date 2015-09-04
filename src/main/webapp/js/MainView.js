/****************************************************************************
 * Fri Apr 17 12:18:45 2009    METODO DEPRECATO
 *
 * Invia la richiesta di caricamento della query e ridisegna l'html con ExtJs
 * Questo é il VECHIO metodo di renderizzare le tabelle HTML con ExtJS.
 * Ora viene costruito tutto il JSON lato server, columnModel e Rows.
 */
function runReportRequest(params) {
    this.params = params;
    Ext.get('principale').update('<br/><br/><center><img src="img/bigloading.gif"/><h2>Loading ...</h2></center>');

    Ext.Ajax.request({
        //url : 'makeReportQuery.jsp',
        url: 'http://' + HOST + '/autogrid/auto-json.jsp',
        success: function (response) {
            // Prendo l'output HTML e lo metto nel <div>
            Ext.get('principale').update('');
            Ext.get('principale').update(response.responseText);

            // Trasforma l'HTML nel <div> in Ext.GridPanel
            var grid = new Ext.grid.TableGrid("tabella-report", {
                stripeRows: true // stripe alternate rows
            });
            grid.render();

            // Aggiungo il bottone, lo aggiungo dopo il caricamento alrimenti
            // finisce anche nel foglio Excel
            var dh = Ext.DomHelper;
            if (Ext.get('id-report-submit') != null) {
                dh.append('id-report-submit', {
                    html: '<input type="submit" name="report" value="Esporta in formato Excel">'
                });
            }

        },
        failure: function () {
            Ext.MessageBox.alert("Errore di caricamento");
        },
        /*headers : {
         'content-disposition' :	'attachment;filename="'+ n.text + '.xls"',
         'content-Type' :	'application/vnd.ms-excel; charset=ISO-8859-1'
         },*/
        params: this.params
    })
}
;

/******************************************************************************
 * Funzione di ricerca per modulo Report. Deprecata, usare loadGrid()
 */
function ricerca_report(query_id, report_title, field_name, field_value) {
    var ricercaQuery = {
        action: 'run',
        idQuery: query_id,
        title: report_title,
        field: field_name,
        value: field_value
    };
    runReportRequest(ricercaQuery);
}
/*********************************************************
 * Bottone che da una form riporta l'utente sulla griglia
 * precedente
 */
BackButton = Ext.extend(Ext.Button, {
    constructor: function (config) {
        // Reusable config options here, invoked when
        // a new instance is created
        Ext.apply(this, {
            text: 'Back',
            formBind: true,
            iconCls: 'arrow-left'
        });
        // super 
        BackButton.superclass.constructor.apply(this, arguments);
    },
    newMethod: function (parm) {
        alert("inside newMethod: " + parm);
    }
});


/******************************************************************************
 * Menu Principale Orizzontale
 */
Ext.onReady(function () {
    // Orologio
    var clock = new Ext.Toolbar.TextItem('');
    var sessionTime = new Ext.Toolbar.TextItem('');
    Ext.TaskMgr.start({
        run: function () {
            Ext.fly(clock.getEl()).update(new Date().format('g:i:s A'));
            //TODO maxInactiveInterval =  maxInactiveInterval - 1;
            //TODO Ext.fly(sessionTime.getEl()).update(maxInactiveInterval);
        },
        interval: 1000
    });

    /**
     * Se l'utente non ha ruolo SuperUser, nasconde parte del menu
     */
    var notSuperUser = false;
    if (Ext.get(RUOLO_SUPERUSER) == null)
        notSuperUser = true;

    // Menu dei report     
    var shellMenu = new GenericMenu({
        id: "shellMenu",
        url: "menus/reports.jsp", //TODO
        loadOnClick: false,
        clickHandler: function (menuItem) {
            var queryId = getIdFromText(menuItem.id);
            //var myUrl = "http://"+HOST+"/charts/"+ type +"_chart.jsp?id="+idGraph;
            var myUrl = "http://" + HOST + "/status/systemStatus.jsp"

            var win = new Ext.Window({
                title: "System status"
                        //,id: 'graph-window'+idGraph
                , modal: false
                , closable: true
                , resizable: true
                , width: 900
                , collapsible: true
                , height: 550
                , layout: 'auto'
                , plain: false
                , autoLoad: {
                    url: myUrl
                }
            });
            win.show();
        }
    });

    /*
     * Menu per visualizzare i grafici: TODO togliere GLOBALE
     */
    databaseMenu = new GenericMenu({
        id: "databaseMenu",
        //url:"menus/charts.jsp", 
        timeout: 180000,
        url: "menus/databases.jsp",
        clickHandler: function (menuItem) {
            // MenuItem se' l'ultima foglia
            var table_info = getIdFromText(menuItem.id).split("##");

            //per il 3° livello del menu var title = menuItem.parentMenu.parentMenu.parentMenu.activeItem.text +
            var title = menuItem.parentMenu.parentMenu.activeItem.text +
                    menuItem.title;

            var idWindow = table_info[0];
            var showDatabaseName = table_info[1];
            var showTableName = table_info[2];
            var hostName = table_info[3];

            var sQL = new ScriptSQL();
            sQL.viewTable({
                action: 'runDefaultScript',
                idScript: "1", //Textarea di default
                title: "Host:" + hostName + "DB: " + showDatabaseName + "Tabella: " + showTableName,
                //tableName: "DM_QUERY_T",
                showTableName: showTableName,
                showDatabaseName: showDatabaseName,
                hostName: hostName
            });

            /**********************
             * Rchiama la pagina che costruisce lato server tutto il JSON che serve
             * per la costruzione della griglia del Report
             var url = "http://"+HOST+"/autogrid/auto-json.jsp";
             var div = "principale";
             var config = {};
             var myGrid = new AutoGrid(div, url, params, config); 
             myGrid.getJSONGrid(); 
             ************************/


        }
    });

    // Prendo l'ID del defect
    function getIdFromText(text) {
        var subStr = text.split("-");
        return subStr[subStr.length - 1];
    }

    //TODO eliminare globale
    saveFilterMenu = new GenericMenu({
        id: "saveFilterMenu",
        url: "menus/filtri.jsp",
        //remoteReload: true,
        clickHandler: function (menuItem) {
            /**
             * Rchiama la pagina che costruisce lato server tutto il JSON che serve
             * per la costruzione della griglia del Report
             */
            //var idFlusso = menuItem.parentMenu.parentMenu.activeItem.text;
            var table_info = getIdFromText(menuItem.id).split("##");

            //var gridTitle = "Filtro: " + menuItem.title; 
            //var filtroId = getIdFromText(menuItem.id);
            var filtroId = table_info[0];
            var showDatabaseName = table_info[1];
            var showTableName = table_info[2];
            var hostName = table_info[3];

            var sQL = new ScriptSQL();
            sQL.viewTable({
                action: 'runSavedScript',
                idScript: filtroId,
                title: "Saved report - Host:" + hostName + "DB: " + showDatabaseName + "Tabella: " + showTableName,
                //tableName: "DM_QUERY_T",
                showTableName: showTableName,
                showDatabaseName: showDatabaseName,
                hostName: hostName
            });

            /**
             if (idFlusso == "Report")
             {
             // Load report with auto-grid
             var url = "http://"+HOST+"/autogrid/auto-json.jsp";
             var div = "principale";
             var config = {};
             var params = {
             action: 'run', 
             idQuery : filtroId, 
             title : gridTitle,
             tableName: "msq_FILTRI_T"
             };
             
             var myGrid = new AutoGrid(/*div * /, url, params, config); 
             myGrid.getJSONGrid();
             }
             else
             {
             // Load filters on grid columns
             var gridName =  idFlusso.toLowerCase() + "Grid";
             
             if (idFlusso == "Problem")    idFlusso = ID_FLUSSO_PROBLEM;
             if (idFlusso == "Workaround") idFlusso = ID_FLUSSO_WORKAROUND;
             if (idFlusso == "Defect")     idFlusso = ID_FLUSSO_DEFECT;
             if (idFlusso == "Task")       idFlusso = ID_FLUSSO_TASK;
             
             var gridConf = {
             title: gridTitle,
             idFiltro: filtroId
             };
             // Il primo caricamento non é ancora filtrato 
             if ( typeof($$(gridName) == "undefined") ){
             if (idFlusso == ID_FLUSSO_PROBLEM)  {  viewProblems(gridConf);    }
             if (idFlusso == ID_FLUSSO_WORKAROUND){ viewWorkarounds(gridConf); }
             if (idFlusso == ID_FLUSSO_DEFECT)   {  viewDefects(gridConf);     }
             if (idFlusso == ID_FLUSSO_TASK)     {  viewTasks(gridConf);       }
             }
             // Resetto i filtri della griglia
             $$(gridName).filters.clearFilters();
             
             var myParams = {
             action: 'run', 
             idQuery : filtroId, 
             title : gridTitle,
             tableName: "msq_FILTRI_T",
             field: "DESCRIIZIONE"
             };
             // Carico il filtro dal DB
             Ext.Ajax.request({
             url: "http://"+HOST+"/autogrid/table-field-json.jsp",
             success: function(response){ 
             //alert("  response.responseText: " + response.responseText);
             TmpFlussoFilters =  Ext.util.JSON.decode(response.responseText);
             
             if (typeof(TmpFlussoFilters.error) != "undefined"){ 
             Ext.MessageBox.alert( "Errore di caricamento del filtro: " +  TmpFlussoFilters.error );
             return;
             }
             // Setto in sessione e sulla griglia il filtro appena caricato 
             SessionFilters[idFlusso] = TmpFlussoFilters;
             // Refresh della griglia 
             if (idFlusso == ID_FLUSSO_PROBLEM)  {  
             viewProblems(gridConf);    
             }
             else if (idFlusso == ID_FLUSSO_WORKAROUND) { 
             viewWorkarounds(gridConf); 
             }
             else if (idFlusso == ID_FLUSSO_DEFECT)   {  
             viewDefects(gridConf);     
             }
             else if (idFlusso == ID_FLUSSO_TASK)     {  
             viewTasks(gridConf);       
             }
             }, 
             failure: function(){ Ext.MessageBox.alert("Errore di caricamento del filtro salvato" ); },
             params: myParams
             });
             }
             ********************************/
        }
    });

    // Menu contestuale sul menu 
    var menuContextMenu = new Ext.menu.Menu({
        id: 'filterContextMenu',
        items: [{
                text: 'Delete ...',
                handler: function (item) {
                    try {
                        var text = item.parentMenu.parentMenu.activeItem.id;
                        var idFiltro = getIdFromText(text);
                        var ajaxParams = {"ID": idFiltro};
                        // Apro la finestra per la cancellazione di un filtro
                        var deleteFilterWin = new FilterMngr.deleteWin({
                            submitParams: {"ID": idFiltro}
                        });
                        deleteFilterWin.show(this);
                    } catch (e) {
                        Ext.MessageBox.alert('Attenzione', 'Selezionare un filtro da cancellare');
                    }
                }
            }]
    });

    //TODO incapsulate into GenericMenu
    saveFilterMenu.on("show", function (e) {
        // Menu already rendered
        if (this.menuCreated) {
            // Loop on menu
            this.items.each(function (item) {
                try {
                    // Handle subMenu
                    if (typeof (item.menu) !== "undefined") {
                        // Loop on all subMenu
                        var collection = item.menu.items;
                        for (var i = 0; i < collection.getCount(); i++) {
                            var myItem = collection.get(i);
                            // Set contextmenu event on subMenu item
                            Ext.getCmp(myItem.id).getEl().on('contextmenu', function (e) {
                                var xy = e.getXY();
                                e.stopEvent();
                                menuContextMenu.showAt(xy, item.menu);
                            });
                        }
                    }
                } catch (e) {
                    //alert(" myItem.id: " + myItem.id + " text: " + myItem.text);
                    //alert("MainView.js saveFilterMenu exception: " + e);
                }
            });
        }
    });


    var filterMenu = "";
    var geoWin, geoBlock = Ext.Element.get(Ext.DomQuery.select("div.execute")[0]);
    var geoContainer = Ext.Element.get(Ext.DomHelper.append(geoBlock, {tag: "div"}));


    /************* Interactive Report Menu for ALL !
     if (USER_GROUP != "Administrator" ){
     shellMenu.add( new InteractiveSQLWindow() );
     
     }
     shellMenu.add( 
     new Ext.menu.Item({
     text: "Interactive query",
     id: 'reportQuery',
     handler: function(){
     ScriptSQL.openWindow();
     }
     })
     );
     *************/
    Ext.IframeWindow = Ext.extend(Ext.Window, {
        onRender: function () {
            this.bodyCfg = {
                tag: 'iframe',
                src: this.src,
                cls: this.bodyCls,
                style: {
                    border: '0px none'
                }
            };
            Ext.IframeWindow.superclass.onRender.apply(this, arguments);
        }
    });

    shellMenu.add({
        text: "MineSQL",
        menu: new Ext.menu.Menu({
            items: [{
                    text: 'New Connection',
                    id: 'shell-1',
                    handler: function () {
                        var connWin = new DatabaseMngr.saveWin({});
                        connWin.show(this);
                    }
                },{
                    text: 'Open Connection',
                    id: 'shell-2',
                    handler: function () {
                        var sql = ScriptSQL();
                        sql.openWindow();
                    }
                }]
        })
    });
    /********
     shellMenu.add({
     text: "GoogleAPI",
     menu: new Ext.menu.Menu({
     items:[{
     text: "Prediction",
     id: 'gogl-api',
     handler: function(){
     var w = new Ext.IframeWindow({
     id:id,
     width:1000,
     height:680,
     title: "",
     src: "/mineSQL/googleAPI"
     })
     w.show();
     
     }
     }]
     })
     });
     **************/
    shellMenu.add({
        text: "Test",
        menu: new Ext.menu.Menu({
            items: [{
                    text: "Map Hilight Example",
                    id: 'map-test-1',
                    handler: function () {
                        var w = new Ext.IframeWindow({
                            width: 840,
                            height: 680,
                            title: "Map Hilight Example",
                            src: "/map-hilight/demo_world.html"
                        })
                        w.show();
                    }
                }, {
                    text: "Tabella con paginazione",
                    id: 'map-tab-1',
                    handler: function () {
                        var w = new Ext.IframeWindow({
                            width: 840,
                            height: 680,
                            title: "Tabella con paginazione",
                            src: "http://www.datatables.net/examples/server_side/simple.html"
                        })
                        w.show();
                    }
                }, {
                    text: "Tabella con scroll",
                    id: 'map-tab-2',
                    handler: function () {
                        var w = new Ext.IframeWindow({
                            width: 840,
                            height: 680,
                            title: "Tabella con scroll",
                            src: "http://www.datatables.net/release-datatables/extensions/Scroller/examples/server-side_processing.html"
                        })
                        w.show();
                    }
                }, {
                    text: "Tabella con scroll",
                    id: 'map-tab-2',
                    handler: function () {
                        var w = new Ext.IframeWindow({
                            width: 840,
                            height: 680,
                            title: "Template Foundation con Dataset",
                            src: "http://datatables.net/dev/foundation.html"
                        })
                        w.show();
                    }
                }, {
                    text: "Data Visualization",
                    id: 'map-tree',
                    handler: function () {
                        var w = new Ext.IframeWindow({
                            width: 900,
                            height: 480,
                            title: "D3 Data-Driven Document",
                            src: "http://mbostock.github.io/d3/talk/20111018/treemap.html"
                        })
                        w.show();
                    }
                }, {
                    text: "Angular JS - RockScissorPaper",
                    id: 'map-tree',
                    handler: function () {
                        var w = new Ext.IframeWindow({
                            width: 900,
                            height: 480,
                            title: "Demo App - Angular JS",
                            src: "/ng-app/"
                        })
                        w.show();
                    }
                }]
        })
    });

    /**********
     TODO: in index.jsp ri-includere geo-ext.js
     shellMenu.add(
     new Ext.menu.Item({
     text: "GeoExt Example",
     id: 'geo-ext',
     handler: function(){
     if(!geoWin) {
     geoWin = new Ext.Window({
     title: "GeoExt in Action",
     height: 400, 
     width: 500,
     layout: "fit",
     closeAction: "hide",
     items: [{
     xtype: "gx_mappanel",
     layers: [new OpenLayers.Layer.WMS(
     "Global Imagery", "http://maps.opengeo.org/geowebcache/service/wms",
     {layers: "bluemarble"}
     )],
     zoom: 1
     }]
     });
     }
     geoWin.show(geoContainer);
     
     }
     })
     );
     ***********/

    // Contenitore della pagina
    var viewport = new Ext.Viewport({
        id: 'viewport',
        layout: 'border',
        items: [
            {
                region: 'north',
                contentEl: 'intestazione',
                split: true,
                height: 100,
                minSize: 100,
                maxSize: 200,
                collapsible: true,
                title: DM_VERSION,
                margins: '0 0 0 0',
                listeners: {
                    'collapse': function () {
                        Ext.getCmp('viewport').fireEvent('resize');
                    },
                    'expand': function () {
                        Ext.getCmp('viewport').fireEvent('resize');
                    }
                }
            }, {
                region: 'center',
                contentEl: 'corpo',
                autoScroll: true,
                collapsible: false,
                margins: '0 5 0 3',
                tbar: [{
                        text: 'Users admin',
                        handler: function () {
                            viewUsersToAble('');
                        },
                        disabled: notSuperUser,
                        iconCls: 'user'
                    }, '-', {
                        /********************
                         text:'Problem',
                         handler: function(){ viewProblems(''); },
                         iconCls: 'icon-grid'
                         },{
                         text:'Workaround',
                         id: "woMenu",
                         handler: function(){ viewWorkarounds(''); },
                         iconCls: 'icon-grid'
                         },{
                         text:'Defect',
                         handler: function(){ viewDefects(''); },
                         iconCls: 'icon-grid'
                         },{
                         text:'Task',
                         handler: function(){ viewTasks(''); },
                         iconCls: 'icon-grid'
                         *********/
                        text: 'Shell',
                        menu: shellMenu,
                        iconCls: 'report'
                    }, '-', {
                        text: 'Databases',
                        iconCls: 'find',
                        menu: databaseMenu
                    }, {
                        text: 'Saved Query',
                        menu: saveFilterMenu,
                        iconCls: 'icon-grid'
                    },
                    '->',
                    'Gruppo:&nbsp;<font color="#000000"><b>' + USER_GROUP + '</b></font>&nbsp;',
                    new Ext.form.ComboBox({
                        store: new Ext.data.SimpleStore({
                            fields: ['idgruppo', 'nomegruppo'],
                            data: gruppiDisponibili
                        }),
                        valueField: 'idgruppo',
                        id: 'change-group',
                        displayField: 'nomegruppo',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText: 'Cambia gruppo...',
                        selectOnFocus: true,
                        width: 135,
                        listeners: {
                            'select': function (combo, record, index) {
                                document.groupForm.idgruppo.value = this.getValue();
                                document.groupForm.submit();
                            }
                        }
                    }),
                    '-',
                    {
                        text: 'System',
                        iconCls: 'info',
                        menu: {items: [{
                                    text: "Application Log",
                                    id: 'cont-int-1',
                                    handler: function () {
                                        var w = new Ext.IframeWindow({
                                            width: 940,
                                            height: 680,
                                            text: "Application Log",
                                            src: "/log/mineSQL.log"
                                        })
                                        w.show();
                                    }
                                }, {
                                    text: "System status",
                                    id: 'sys-status',
                                    handler: function () {
                                        var w = new Ext.IframeWindow({
                                            id: id + "system",
                                            width: 840,
                                            height: 680,
                                            title: "View system status",
                                            src: "/status/systemStatus.jsp"
                                        })
                                        w.show();
                                    }
                                }, {
                                    text: "PHP Quercus status",
                                    id: 'quercus-status',
                                    handler: function () {
                                        var w = new Ext.IframeWindow({
                                            id: id + "quercus",
                                            width: 840,
                                            height: 680,
                                            title: "View PHP Quercus status",
                                            src: "/status/phpinfo.php"
                                        })
                                        w.show();
                                    }
                                },
                                {text: 'Host:&nbsp;<font color="#000000"><b>' + HOST_NAME + '</b></font>&nbsp;'
                                },
                                {text: 'User:&nbsp;<font color="#000000"><b>' + USER_NAME + '</b></font>&nbsp;'
                                },
                                {text: 'Reparto:&nbsp;<font color="#000000"><b>' + USER_NOME_REPARTO + '</b></font>&nbsp;'
                                },
                                {text: 'Ruolo:&nbsp;<font color="#000000"><b>' + USER_NOME_RUOLO + '</b></font>&nbsp;'
                                }
                            ]
                        }
                    },
                    '-',
                    {iconCls: 'time', id: 'ora'},
                    ' ', clock
                ]
            }
        ],
        listeners: {
            'resize': function () {
                /*resize_form(this, 'problemForm');
                 resize_form(this, 'workaroundForm');
                 resize_form(this, 'defectForm');
                 resize_form(this, 'taskForm');*/
            }
        }
    });
});

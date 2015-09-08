// vim: ts=4:sw=4:nu:fdc=4:nospell
/**
 * Genera una finestra che contiene una form con TUTTI i campi della tabella
 * richiesta. Il chiamante puo specificare anche i campi, in questo caso saranno
 * presenti SOLO i campi specificati
 *
 * @param String tableName Nome della tabella sul DB del defect
 * @param String url indirizzo della pagina che restituisce il fieldSet della tabella
 * @param Ext.form.fieldSet items Puo contenere un Ext.form.fieldSet, altrimenti viene richiamato l'url 
 *
 * @author    Finamore Alessio
 * @copyright (c) 2010, by Assioma.net
 * @date      29 / 04 / 10
 * @version   0.1
 * @revision  $Id: CSVMngr.js,v 1.7 2009/07/30 16:35:37 assioma Exp $
 *
 * @license licensed under the terms of
 * the Open Source LGPL 3.0 license. Commercial use is permitted to the extent
 * that the code/component(s) do NOT become part of another Open Source or 
 * Commercially licensed development library or toolkit 
 * without explicit permission.
 * 
 * <p>License details: <a href="http://www.gnu.org/licenses/lgpl.html"
 * target="_blank">http://www.gnu.org/licenses/lgpl.html</a></p>
 */
WindowFormTable = Ext.extend(Ext.Window,{
    /**
     * Costruttore della Window
     * @param Object conf Configura i campi della form
     *
     * @return Ext.Window 
     */
    constructor : function(conf){
        //alert(" entry constructor");

        // Salvo le proprieta aggiuntive delle oggetto 
        this.config = conf; 
	// Visualizza l'alert con l'esito del submit, se settato a false
	// l'alert non viene visualizza, la windowForm non viene chiusa
	// e sono cazzi del chiamante a cui viene forwardata il result
	if (typeof conf.showMessage !== undefined )
		this.config.showMessage = conf.showMessage;
	else
		this.config.showMessage = true;
    // la form caricata lato server
    this.myFormPanel = undefined;
    this.versione = "bho";
    this.formFieldRequest = undefined;
    this.staCaricando = true;

	Ext.Ajax.timeout = 120000;
	//alert(" MARK WindowFormTable.js set timeout to 120000");
        // Default configuration to extend Ext.Window
        var defaultConf = {
                    title: conf.title,
                    modal: conf.modal || false,
                    closable:true,
                    width: ((conf.width)?conf.width:450),
                    height: ((conf.height)?conf.height:'auto'),
                    //autoHeight: ((conf.height)?false:true),
                    border:false,
                    collapsible: true && ((conf.collapsible == false)?false:true),
                    resizable: conf.resizable || false,
                    plain:true,
                    layout: 'fit'
        };

        try{
            var _saveMe = this;
            // Se non viene passato un fieldSet allora lo richiedo lato server
            if (! conf.items){
                _saveMe.staCaricando = true;
                alert("MARK1: " + conf.tableName);
                //TODO var ajaxParams = {column: true, meta: true};
                            //alert("before request success : " + _saveMe.staCaricando);
                _saveMe.formFieldRequest = Ext.Ajax.request({
                        url: conf.url,
                        success: function(response){ 
                            //alert("staCaricando 1 success : " + _saveMe.staCaricando);
                            _saveMe.add( _saveMe.ajaxFieldSet(conf, response) );
                            _saveMe.render();
                            _saveMe.staCaricando = false;
                            //alert("staCaricando 2 success : " + _saveMe.staCaricando);
                        }, 
                        failure: function(){ 
                            Ext.MessageBox.alert("Errore Ajax", "Errore di  caricamento del fieldSet dall'url: " +  conf.url); 
                            _saveMe.staCaricando = false;
                            //alert("staCaricando ailure: " + _saveMe.staCaricando);
                        },
                        params: {
                                "tableName" : conf.tableName,
                                "crudOperation" : "fieldSet" 
                        }
                });
                // TODO nel caso di read/delete uguale per il conf.reader, fare chiamate sequenziali per ora
            }else{
                defaultConf.items = _saveMe.buildFormTable(conf);
            }

            // Extend Window compenent with my WindowFormTable
            Ext.apply(_saveMe, defaultConf);
            WindowFormTable.superclass.constructor.call(_saveMe);
           
            //alert(" Alla fine del costruttore"); 
        }catch(e){
            alert(" WindowFormTable  exception: " + e);
        }
    },
    // Traduce la ripsosta ajax nel fieldSet, la costruzione del fieldSet JSON
    // avviene lato server 
    ajaxFieldSet: function(conf, serverResponse){
                    try{
                    var JSONstring = eval("("+ serverResponse.responseText  +")");
                    conf.items = JSONstring/*.items*/;
                    }catch(e){
                        alert(" eval exception :" + e);
                    }
                    console.log("Server response items: ");
                    console.log( conf);
                    //conf.reader = JSONstring.reader;
                    return this.buildFormTable(conf);
    },
    // Attacca un fieldSet alla form
    buildFormTable : function(conf){
                    this.versione = "0.1";
                    var formTable = new Ext.FormPanel({ 
                                            labelAlign: 'right',
                                            frame:true,
                                            region: 'center',
                                            autoHeight:((conf.resizable)?false:'auto'),
                                            autoScroll:true,
                                            labelWidth: 85,
                                            waitMsgTarget: true,
                                            items: [conf.items],
                                            reader: conf.reader 
                    });
                    var _saveMe = this;
                    var _url = conf.url;
                    var _buttonText = "";
                    var _waitMsg = "";
                    // Parametri da POSTARE obbligatoriamente
                    var _submitParams ={
                        "tableName" : conf.tableName,
                        "crudOperation" : conf.crud
                    };
                    // Merge dei parametri di submit passati dal chiamante
                    if (typeof(conf.submitParams) !== "undefined"){
                        Ext.apply(_submitParams, conf.submitParams);
                    }
                    // Configuro il layout della form a seconda dell'operazione
                    switch(conf.crud){
                        case 'create':
                            _waitMsg = 'Saving Data...';
                            _buttonText="Save";
                            break;
                        case 'read':
                            _waitMsg = 'Saving Data...';
                            _buttonText="Close"; 
                            break;
                        case 'update':
                            _waitMsg = 'Saving Data...';
                            _buttonText="Update";
                            break;
                        case 'delete':
                            _waitMsg = 'Deleting Data...';
                            _buttonText="Delete";
                            break;
                    }
                    var submit = formTable.addButton({
                            text: _buttonText,
                            disabled:false,
                            handler: function(){
                                try{

                                if (typeof(conf.onBeforeSubmit) !== "undefined")
                                    conf.onBeforeSubmit();

                                //TODO gestire errori con xml-errors.xml
                                formTable.getForm().submit({
                                    url: _url,
                                    waitMsg: _waitMsg,
                                    params: _submitParams,
                                    success:function(form, action) {
						                    if (_saveMe.config.showMessage ){
                                				if (action.result.reason != 'no-popup') {
                                					Ext.Msg.alert('Status', action.result.reason, function(btn){
                                						_saveMe.close();
                                                        if (typeof(conf.onAfterSubmit) !== "undefined")
                                						    conf.onAfterSubmit(action.result);
                                					});
                                				}
                                            }else{
                                                _saveMe.close();
                                                if (typeof(conf.onAfterSubmit) !== undefined)
                                                    conf.onAfterSubmit(action.result);
                                            }
                                    },
                                    failure:function(form, action){
                                                Ext.MessageBox.alert('Error', action.result.reason);
                                    }                        
                                });
				    }catch(e){
                                                Ext.MessageBox.alert('Exception Error', e );
				    }
                            }
                    });
                    // Load form data:  anche l'update ricarica i dati dal DB tramite la chiave primaria
                    if (conf.crud == "read" || conf.crud == "delete" ||  conf.crud == "update"){
                        submit.disable();
                        /*** TODO
                        fs.addButton('Load', function(){
                            fs.getForm().load({url:'xml-form.xml', waitMsg:'Loading'});
                        });
                        ***/
                        // per caricare i dati forzo sempre la read
                        var _loadParams ={
                            "tableName" : conf.tableName,
                            "crudOperation" : "read"
                        };
                        alert("MARK0: " + conf.tableName);
                        Ext.apply(_loadParams, conf.submitParams);
                        formTable.getForm().load({
                                url:_url,
                                params: _loadParams
                        });
                        formTable.on({
                            actioncomplete: function(form, action){
                                if(action.type == 'load'){
                                }
                                submit.enable();
                            }
                        });
                    }
                    this.myFormPanel=formTable;
                    //alert(" this.versione:" + this.versione + " _saveMe.versione:" + _saveMe.versione);
                    this.versione = "fine costruzione form this";
                    _saveMe.versione = "fine costruzione form _saveMe";
                    //alert(" this.versione:" + this.versione + " _saveMe.versione:" + _saveMe.versione);

                    return formTable;
    },
    getFormTable : function(conf){
                    var tId =  this.formFieldRequest.tId;
                    function wait(millis)
                    {
                        var date = new Date();
                        var curDate = null;

                        do { curDate = new Date(); }
                        while(curDate-date < millis);
                    }

                    //alert("staCaricando: " + this.staCaricando );
                    var i = 1;
                    do{
                        //alert("waiting staCaricando:" + this.staCaricando);
                        //wait(500);
                        i++;
                    //}while(this.staCaricando);
                    }while(i<10);

                    //alert("getFormTable versione: " + this.versione);
                    //alert("staCaricando: " + this.staCaricando );

                    return this.myFormPanel;
    },
    // Override parent show
    show : function(){
        /**
        switch(this.config.crud){
            case "read":
                alert("read");
                break;
            case "delete":
                alert("crud");
                break;
        }
        this.getForm().load({url:"soka",params:{"uno":"due"}});
        **/

        WindowFormTable.superclass.show.call(this);
    }
});

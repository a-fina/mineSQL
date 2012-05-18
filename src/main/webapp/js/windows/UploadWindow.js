 /**
 * Apre una finestra di dialogo per l'upload di file,
 * gestendo la richiesta da inoltrare al server.
 * @param idflusso
 * @param id
 */
//function uploadFile(idflusso, id, subfolder) {
function uploadFile(property) {

    alert("property UploadWindows");
    console.dir(property.dataStore);
    var msg = function(title, msg){
        Ext.Msg.show({
            title: title,
            msg: msg,
            minWidth: 200,
            modal: true,
            icon: Ext.Msg.INFO,
            buttons: Ext.Msg.OK
        });
    };

    var overwriteMsg = function() {
    	Ext.Msg.show({
    	    title: 'Conferma sostituzione file',
    	    msg: 'Il file indicato è già presente. Sovrascrivere il file esistente?',
    	    buttons: Ext.Msg.YESNO,
    	    fn: function(btn, text) {
    	    	if(btn == 'yes')
    	    		uploadRequest(true);
    	    },
    	    animEl: 'elId',
    	    icon: Ext.MessageBox.QUESTION
        });
    };

    var uploadRequestHandler = function(button, event) {
    	uploadRequest(false);
    }

    var uploadRequest = function(overwrite) {

        var trId = /*"ID_temporaneo_casuale" + */$$("file-name").getValue() ;
        //alert(" filename: " + trId + " encoded: " +  Ext.util.JSON.decode( trId ) );
        var params = { 
            	    idflusso: "idflusso",
		            id: "id",
            	    transactionId: trId,
            	    subfolder: "subfolder",
            	    departement: USER_NOME_REPARTO,
            	    overwrite: overwrite,
                    fileName: trId
        };
        Ext.apply(params, property);

    	if(fp.getForm().isValid()){
            fp.getForm().submit({
                url: 'nas/upload.jsp',
                params: params,
                waitMsg: 'Loading file...',
                success: function(fp, o){
                	var returnMsg = '';
                	// Il file e' stato creato/aggiornato su Sharepoint
                	if (o.result.reason == 'uploaded') {
                		//returnMsg = 'Aggiornamento effettuato.';
                		//msg('Success', returnMsg);
                        //alert("fileName: " + trId + " encoded: " + //Ext.util.JSON.encode(trId));
                        uploadWin.close();
                        var updateCSVWin = new CSVMngr.update({
                            submitParams: params
                        });
                        updateCSVWin.show(this);
                	}
                	// Il file gia' esiste su Sharepoint
                	else if (!overwrite && o.result.reason == 'exist') {
                		overwriteMsg();
                	}
                },
                failure: function(fp, o){
                    msg('Error', o.result.reason);
                    uploadWin.close();
                }
            });
        }
    };

    var fp = new Ext.FormPanel({
        fileUpload: true,
        frame: true,
        //title: 'File Upload Form',
        autoHeight: true,
        //monitorValid: true,
        bodyStyle: 'padding: 10px 10px 0 10px;',
        labelWidth: 40,
        defaults: {
            anchor: '100%',
            allowBlank: false,
            msgTarget: 'side'
        },
        items: [{
            xtype: 'fileuploadfield',
            id: 'file-name',
            emptyText: 'Scegli un file ...',
            fieldLabel: 'File',
            buttonText: '',
            buttonCfg: {
                //iconCls: 'upload-icon'
                iconCls: 'icon-save'
            }
        }],
        buttons: [{
            text: 'Upload',
            type: 'submit',
            formBind: true,
            handler: uploadRequestHandler
        },{
            text: 'Reset',
            handler: function(){
                fp.getForm().reset();
            }
        }]
    });

    var uploadWin = new Ext.Window({
        title: 'Select File',
        closable: true,
        width: 450,
        height: 150,
        modal: true,
        border: false,
        collapsible: true,
        resizable: false,
        plain: true,
        items: [fp]
    });

    uploadWin.show(); 
};

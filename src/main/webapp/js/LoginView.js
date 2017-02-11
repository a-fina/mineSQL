/****************************************************************************
 * Fri Apr 17 12:18:45 2009    METODO DEPRECATO
 *
 * Invia la richiesta di caricamento della query e ridisegna l'html con ExtJs
 * Questo é il VECHIO metodo di renderizzare le tabelle HTML con ExtJS.
 * Ora viene costruito tutto il JSON lato server, columnModel e Rows.
 */


/******************************************************************************
 * Menu Principale Orizzontale
 */
Ext.onReady(function () {
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
                tbar: ['-']
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

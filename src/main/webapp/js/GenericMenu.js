/******************************************************************
 *
 * Menu a tendina generico, viene costruito dopo avere interrogato
 * un URL che risponde con  la struttura JSON del treeMenu
 *
 * @class       GenericMenu
 * @author      Finamore Alessio
 * @copyright   Assioma.net
 * @date         
 * @version     0.1
 * @revision    $Id: GenericMenu.js,v 1.8 2009/07/27 09:07:08 assioma Exp $
 * @param       String    conf.url          indirizzo da richiamare String
 *              String    conf.id           prefisso per l'id degli elementi del menu
 *              Funcition conf.clickHandler funzione callback da chiamare al click, 
 *                                    gli viene passato l'item clickato
 * @return Ext.menu.Menu menuObject 
 *
 */
GenericMenu = (function(conf){

    var _id = conf.id;
    var _url = conf.url;
    var _menuClickHandler = conf.clickHandler;
    var _remoteReload = conf.remoteReload || false;
    var _params = conf.params || {};
    var _loadOnClick = conf.loadOnClick;
  
    //this._alreadyLoaded = false; 

    var menuObject = undefined;
	var id_counter = 0;

    // Funzione ricorsiva che scandisce il JSON ed assembla un menu 
    function create_menu_tree(json_tree, menu){
        try{
            for(var i = 0, len = json_tree.length; i < len; i++){
                //alert("o[i].text: " + json_tree[i].text);
                //alert("o[i].children: " + typeof json_tree[i].children);
                if ( typeof json_tree[i].children == "object") { 
					var id_name = "subMenu"+[i]+"-"+id_counter;
                    var subMenu = new Ext.menu.Menu({id:id_name});
					id_counter++; // FIX per porting ExtJs 3.2.1
                    create_menu_tree(json_tree[i].children, subMenu);
                    // Populate menu items
                    menu.add({
                        text: json_tree[i].text,
                        id: _id +id_name,
                        menu: subMenu
                        });
                }else{
                    // Populate sub menu items
                    menu.add({
                        text: json_tree[i].text,
                        id: _id+"-sub-"+json_tree[i].id,
                        title: json_tree[i].title,
                        handler: (function(item){ _menuClickHandler(item); }) 
                    });
                }
            }
        }catch(e){
            Ext.Msg.alert('GenericMenu.js Exception:' + e);
        }
    }
    // Singleton 
    if ( typeof(menuObject) === "undefined")
        menuObject = new Ext.menu.Menu({ shadow : false,id: _id }); 

    // Run remote request and build the Menu
    if ( typeof(_loadOnClick) === "undefined"  || _loadOnClick ) {
        menuObject.on('beforeshow', function(){
            // Don't reload menu
            if ( typeof(this._alreadyLoaded) == "undefined" || _remoteReload){ 
               
                // Show temporary Loading message
                var loadingMenu = new Ext.menu.Item({ 
                                       text: 'Loading...', 
                                       cls:"loading-indicator",
                                       id:"loadingMenu" 
                                    });

                //this.removeAll(); 
                this.add(loadingMenu);
                var _saveThis = this;

                this.menuCreated = false;
                // Run request
                Ext.Ajax.request({
                    method:"POST",
                    url: _url,
                    params: _params,
                    success: function(response){
                        menuObject.remove(loadingMenu);
                        var a = response.argument;
                        //var node = a.node;
                        //var callback = a.callback;
                        var callback = undefined;
                        var json = response.responseText;

                        try {
                            var o = undefined;
                            try{
                                o = eval("("+json+")");
                            }catch(e){ 
                                Ext.Msg.alert(" Error evaluating JSON", "Exception name: " + e.name + " message: " + e.message);
                            }

                            //node.beginUpdate();
                            if ( typeof(o) !== "undefined" )
                                create_menu_tree(o, menuObject);
                            _saveThis.menuCreated = true;
                            // access items by id or index
                            if (typeof menuObject.items.get(_id+"-sub-"+"disableMe") !== "undefined" ){
                                menuObject.items.get(_id+"-sub-"+"disableMe").disable();
                            }

                            //node.endUpdate();
                            if(typeof callback == "function"){
                                callback(this, node);
                            }
                        }catch(e){
                            //this.handleFailure(response);
                            alert("Exception :" + e + " Response: " + response);
                        }
                    },
                    failure: this.handleFailure
                    //scope: this, probabile utilizzo ricorsivo
                    //argument: {callback: callback, node: node},
                    //params: this.getParams(node)
                });
                this._alreadyLoaded = true;
            }
        });
    }

    return menuObject;		
});

/* 
 * Variabili globali 
 */
var currentSearch = "currentSearch";
var currentQuery = "currentQuery";
var searchMenu;
var searchForm;

// REstituisce il campo di ricerca corrente sul flusso richiesto
function getCurrentSearch(idFlusso){
    debug("getCurrentSearch: " + SessionFilters[idFlusso][currentSearch]);
    if (typeof SessionFilters[idFlusso][currentSearch] !== "undefined"  )
        return SessionFilters[idFlusso][currentSearch];
    else 
        return "";
}
/*******************************************************************************
 * Definisce il menu per la ricerca nelle griglie. Le voci a menu sono i campi
 * della tabella e sono passati INPUT: searchItems, sono i campi su cui
 * effettuare la ricerca
 ******************************************************************************/
function defineSearch(currentStore, gridstartPage, campiRicerca, idFlusso) {
	// Azione onClick sul filtro
	function onSearchItemCheck(item, checked) {
		if (checked) {
			Ext.get('filterlabel').update('['+item.text+']');
			SessionFilters[idFlusso][currentSearch] = item.text;
		}
		gridstartPage = 0;
	}

	// Crea il componente menu
	searchMenu = new Ext.menu.Menu({
		id : 'searchMenu'
	});
	// Ci metto gli items
    var defaultSearch = "";
	Ext.each(campiRicerca, function(item, index, allItems) {
        var isEnabled = false;
        if (typeof SessionFilters[idFlusso][currentSearch] == "undefined"){
            if (index == 0) {
                isEnabled = true;
                SessionFilters[idFlusso][currentSearch] = item;
            } 
        } 
		searchMenu.add(new Ext.menu.CheckItem({
			text : item,
			checked : isEnabled,
			group : 'filter',
			id : item,
			checkHandler : onSearchItemCheck
		}));
	});
    return searchMenu;
}

/*******************************************************************************
 * Form per ricerca multicampo sulle griglie
 ******************************************************************************/
function defineSearchForm(currentStore, gridstartPage, campiRicerca,currentGrid) {
	var searchFieldSet = new Ext.form.FieldSet({
		autoHeight : true,
		title : 'Colonne delle lista'
	});
	
	// Costruzione della Form
	var searchForm = new Ext.FormPanel({
		region : 'north',
		frame : true,
		labelWidth : 90,
		items : searchFieldSet,
		defaults : {
			width : 530
		},
		defaultType : 'textfield',
		titlebar : true,
		iconCls : 'find',
		title : 'Ricerca avanzata',
		// collapsedTitle : 'click on the arrows to view filter form ==>',
		collapsible : true,
		collapsed : true
	});
	
	// Ci metto gli items
	Ext.each(campiRicerca, function(item, index, allItems) {
		searchFieldSet.add(
			new Ext.form.TextField({
				fieldLabel : item,
				name : item,
				id : item,
				width : 270
			})
		);
	});
	// Aggiunta Bottoni
	
	var submit = searchForm.addButton({
		text : 'Ricerca',
		// disabled:true,
		handler : function() {
			alert('submit search');
			// Se é checked lo aggiungo alla stringa dei filtri

		Ext.each(searchFieldSet, function(item, index, allItems) {
			debug("Grid ricerca avanzata item: " + item.name + "<-");
		});	
			currentSearch = 'campo1,campo2,campo3';
			// trigger the data store load
			currentStore.load({
				params : {
					start : gridstartPage,
					limit : 20,
					filter : currentFilter,
					query : 'value1,value2,value3'
				 }
			});

		}
	});
	var submit = searchForm.addButton({
		text : 'Reset',
		// disabled:true,
		handler : function() {
			//searchForm.reset();
			Ext.MessageBox("Premuto bottone reset");
			currentStore.clearFilter();
		}
	});
	// Layout manager
	var searchGrid = new Ext.Panel({
		renderTo : 'principale',
		defaults : {
			collapsible : true,
			split : true,
			bodyStyle : 'padding:15px'
		},
		layout : 'column',
		items : [searchForm, currentGrid]
			/*
			 * north: { titlebar: true, title: 'Ricerca Avanzata',
			 * collapsedTitle: 'click on the arrows to view filter form ==>',
			 * collapsible:true, collapsed:true, //hidden:true,
			 * margins:{left:3,top:3,right:3,bottom:0}, initialSize: 150,
			 * split:true },
			 * 
			 * center: { margins:{left:3,top:0,right:3,bottom:3} }
			 */
	});
}

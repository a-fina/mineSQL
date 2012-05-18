/**
* Menu di ricerca campi  
*/
//var currentFilter = "Gruppo,Aperto";
var currentFilter = "Gruppo,In lavorazione";
var filterMenu = "";
/**
 * Array per tenere memoria dei filtri checkati passando da una grid all'altra
 */
var filterArray = new Array();
filterArray['Gruppo'] = true;
//filterArray['Aperto'] = true;
filterArray['In lavorazione'] = true;

function defineFilter(currentStore, gridstartPage){
    /**
    * Azione onClick sul filtro
    */ 
    function onFilterCheck(item, checked){

        // Se é checked lo aggiungo alla stringa dei filtri
        if (checked){ 
            currentFilter += "," + item.text;
            filterArray[item.text] = true;
        }else{
            currentFilter = currentFilter.replace(item.text,"");
            filterArray[item.text] = false;
        }
        currentFilter = currentFilter.replace(",,",",");
        currentFilter = currentFilter.replace(/^,/,"");
        currentFilter = currentFilter.replace(/,$/,"");
		gridstartPage = 0;
		
            debug("3 onFilterCheck CurrentFilter: ->"+ currentFilter +"<-");
        // trigger the data store load
        currentStore.load({
            params:{
                start: gridstartPage,
                limit: 20, 
                filter: currentFilter/*,
                sort:'IDPROBLEM', 
                dir:'ASC'*/}
        });
    }

    /**
    *  Bottone Menu sulla griglia
    */ 
    filterMenu = new Ext.menu.Menu({
            text: 'Filtra per:',
            id: 'mainMenu'
    });
    
    // Per il ruolo ReadOnly non deve essere possibile vedere
    // elementi degli altri gruppi
    if ( ! isLimitedUser() ){ 
        filterMenu.add({
                id: 'changeGroup',
                text: 'Gruppo',
                checked: filterArray['Gruppo'],
                checkHandler: onFilterCheck
        }, '-');
    }
    filterMenu.add({
            text: 'In lavorazione',
            checked: filterArray['In lavorazione'],
            checkHandler: onFilterCheck
    });
}

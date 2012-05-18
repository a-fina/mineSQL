var months_it = {"gen":"1", "feb":"2", "mar":"3", "apr":"4", "mag":"5", "giu":"6", "lug":"7", "ago":"8", "set":"9", "ott":"10", "nov":"11", "dic":"12"};
var months_en = {"jan":"1", "feb":"2", "mar":"3", "apr":"4", "may":"5", "jun":"6", "jul":"7", "aug":"8", "sep":"9", "oct":"10", "nov":"11", "dec":"12"};

Ext.override(Ext.form.DateField, {
			allowFutureDate : true,
            parseDate : function(value){
                if(!value || value instanceof Date){
                    return value;
                }
                if(this.preValidateDate(value, this.format)){
                    var v = Date.parseDate(value, this.format);
                }
                if(!v && this.altFormats){
                    if(!this.altFormatsArray){
                        this.altFormatsArray = this.altFormats.split("|");
                    }
                    for(var i = 0, len = this.altFormatsArray.length; i < len && !v; i++){
                        if(this.preValidateDate(value, this.altFormatsArray[i])){
                            v = Date.parseDate(value, this.altFormatsArray[i]);
                        }
                    }
                }
                return v;
            },
            preValidateDate : function(value, format){
                var d, m, y, r = /[-\/\\.]/;
                var valueParts = value.split(r);
                var formatParts = format.split(r);

                if(!valueParts || !formatParts){
                    return false;
                }
                // Day, Month and Year are mandatory (three date component)
                if (valueParts.length != 3) {
                	return false;
                }
                // Check the three date component
               	for(var i = 0; i < formatParts.length; i++){
               		switch(formatParts[i]){
                   		case 'M': // month like 'jan', 'feb' ... (ignorecase)
                   			var key = valueParts[i].toLowerCase();
                   			if (typeof(months_en[key]) != 'undefined')
                   				m = months_en[key];
                   			else if (typeof(months_it[key]) != 'undefined')
                    			m = months_it[key];
                    		break;
                        case 'm': //leading 0
                        case 'n': //no leading 0
                            m = valueParts[i];
                            break;
                        case 'd': //leading 0
                        case 'j': //no leading 0
                            d = valueParts[i];
                            break;
                        case 'y': //2-digit
                        case 'Y': //4-digit
                            y = valueParts[i];
                            break;
                    }
                }
                if(m && (m < 1 || m > 12)){
                    return false;
                }
                if(d){
                    y = y || new Date().getFullYear();
                    var isLeapYear = ((y & 3) == 0 && (y % 100 || (y % 400 == 0 && y)));
                    var daysInMonth = [31,(isLeapYear ? 29 : 28),31,30,31,30,31,31,30,31,30,31];
                    m = m ? m-1 : (new Date().getMonth() - 1);
                    var days = daysInMonth[m];
                    if(d < 1 || d > days){
                        return false;
                    }
                }

                return true;
            },
            validator: function(value) {
            	if (!this.allowFutureDate && (this.getValue() > new Date()))
            		return 'This date is in the future';
            	else
            		return true;
            }
        });
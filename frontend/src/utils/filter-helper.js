import moment from 'moment'

export default class FilterHelpers {
    static toLocalDateTimeString(value) {
        if (value)
            return moment(String(value)).local().format("YYYY/M/D H:m")
    }

    static toStringEllipsis(value, limitLength = 100) {
        if (value.length < limitLength)
        return value;
      
      return value.substring(0, limitLength-3) + "...";
    }
} 
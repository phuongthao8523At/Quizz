import {Injectable} from '@angular/core';
import {DatePipe} from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class DateService {

  constructor() {
  }

  getFormatDate(value: Date, formatString: string = 'yyyy-MM-dd h:mm') {
    return new DatePipe('en_US').transform(value, formatString) || '';
  }
}

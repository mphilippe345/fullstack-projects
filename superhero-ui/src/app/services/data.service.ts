import { Injectable } from "@angular/core"
import { BehaviorSubject, Observable } from "rxjs"

@Injectable()
export class DataService {
    private genreSource = new BehaviorSubject<string>('');
    currentGenre = this.genreSource.asObservable();
    public stringData$: Observable<string> = this.genreSource.asObservable();

    constructor() {}

    changeGenre(genre: string){
        this.genreSource.next(genre)
    }

}
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/interfaces/user';
import {API_URL } from 'src/app/constants/constants'

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private client: HttpClient) { }

  concatName(firstName?: string, lastName?: string) {
    return firstName + ' ' + lastName
  }

  getAllUsers(): Observable<User[]> {
    return this.client.get<User[]>(`${API_URL}/api/users`)
  }

  getUserById(id: number): Observable<User> {
    return this.client.get<User>(`${API_URL}/api/users/${id}`)
  }

  updateUser(updatedUser: User, id: number): Observable<User> {
    return this.client.put<User>(`${API_URL}/users/${id}`, updatedUser)
  }

  deleteUserById(id: number): Observable<unknown> {
    return this.client.delete(`${API_URL}/users/${id}`)
  }
 }

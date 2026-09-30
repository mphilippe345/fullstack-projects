export interface User {
    id?: number,
    given_name: string,
    family_name: string,
    email: string,
    password: string,
    verficationCode: string,
    showPassword: boolean,
    role: string
}
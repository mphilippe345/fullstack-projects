import { Injectable } from '@angular/core';
import { CognitoUserPool } from 'amazon-cognito-identity-js';
import { Amplify, Auth } from 'aws-amplify';
import { environment } from 'src/environments/environment';
import { User } from '../interfaces/user';
const poolData = {
  UserPoolId: 'us-east-2_RNdWF28rX',
  ClientId: '7qb7i703kbsdvs7vobgselg68f'
};

const userPool = new CognitoUserPool(poolData);

@Injectable({
  providedIn: 'root'
})
export class CognitoService {
  user: any;
  isLoggedIn(): Promise<boolean> {
    return this.getUser()
      .then((user: any) => {
        if (user) {
          //logged in 
          this.user = user.attributes;
          return true;
        } return false;
      })
      .catch(() => {
        return false;
      }); 
  }

  constructor() {
    // Check if the user is already authenticated when service is initialized
    Amplify.configure({
      Auth:environment.cognito
    })
  }


  public signUp(user: User): Promise<any> {
    return Auth.signUp({
      username: user.email,
      password: user.password,
      attributes: {
        email: user.email,
        given_name: user.given_name,
        family_name: user.family_name,
        'custom:role' : 'customer'
      }
    })
  }

  public confirmSignUp(user: User): Promise<any> {
    return Auth.confirmSignUp (user.email,user.verficationCode)
  }

  //this method will return info if user us logged in with vaild email and password
  public getUser() : Promise<any> {
    return Auth.currentUserInfo();
  }

  public signIn(user: User): Promise<any> {
    return Auth.signIn(user.email, user.password);
  }

  public signOut(): Promise<any> {
    return Auth.signOut();
  }
//this method will sent a new code to user email.
  public forgotPassword( user:User): Promise<any> {
    return Auth.forgotPassword(user.email)
  }
// we submit the new password with email and code sent to that email.
  public forgotPasswordSubmit(user: User, new_password: string): Promise<any> {
    return Auth.forgotPasswordSubmit(user.email, user.verficationCode, new_password)
  }

  googleSocialSignIn(): void {
    Auth.federatedSignIn( { customProvider : 'Google'})
  }
}

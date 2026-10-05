import HttpHelper from '../../utils/HttpHelper';

/**
 * Retrieves a user by email from the database
 * @param {email} email of the user
 * @param {setUser} setUser is a function that helps to retrieve a user by email.
 */
const getUserByEmail = async (email, setUser) => {
  await HttpHelper(`/users/${email}`, 'GET')
    .then((response) => {
      if (response.status === 200) {
        return response.json();
      }
      throw new Error(response.statusText);
    })
    .then((body) => {
      setUser(body);
      document.cookie = `user=${JSON.stringify(body)}`;
    })
    .catch(() => {});
};

/**
 * Updates the user when profile information is changed.
 * @param {user} user to update the profile information
 * @param {setUser} function that updates the user information
 * @param {*} id user id from user data base
 */
const updateUser = async (user, setUser, id) => {
  await HttpHelper(`/users/${id}`, 'PUT', user)
    .then((response) => {
      if (response.status === 200) {
        return response.json();
      }
      throw new Error(response.statusText);
    })
    .then(setUser)
    .catch(() => {});
};
// export { getUserByEmail, updateUser };
export default getUserByEmail;
export { updateUser };

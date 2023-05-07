
const STORE_NAME = 'token';
const REMEMBER_ME = 'remembered';

const getToken = ()=>{
  const remembered = getRememberMe();
  const serializedUser = remembered
      ? localStorage.getItem(STORE_NAME)
      : sessionStorage.getItem(STORE_NAME);
  if (!serializedUser) return undefined;
  return JSON.parse(serializedUser);
}

const clearAuthenticatedUser = () => {
  localStorage.clear();
  sessionStorage.clear();
};

async function getAuthenticatedUser() {

  const token = getToken();
  let user ;
  if (token){
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization : token
      },
    };

    const response = await fetch(`${process.env.API_BASE_URL}/users/user`, options);
    // if the user retrieved with the token is null then the user is redirected to the login page
    if (!response.ok){
      clearAuthenticatedUser();
    }


    user = await response.json();
  }


  return user;
}



const setAuthenticatedUser = (authenticatedUser) => {
  const serializedUser = JSON.stringify(authenticatedUser.token);
  const remembered = getRememberMe();
  if (remembered) localStorage.setItem(STORE_NAME, serializedUser);
  else sessionStorage.setItem(STORE_NAME, serializedUser);


};





function getRememberMe() {
  const rememberedSerialized = localStorage.getItem(REMEMBER_ME);
  const remembered = JSON.parse(rememberedSerialized);
  return remembered;
}

function setRememberMe(remembered) {
  const rememberedSerialized = JSON.stringify(remembered);
  localStorage.setItem(REMEMBER_ME, rememberedSerialized);
}

export {
  getAuthenticatedUser,
  setAuthenticatedUser,
  clearAuthenticatedUser,
  getRememberMe,
  setRememberMe,
    getToken,
};

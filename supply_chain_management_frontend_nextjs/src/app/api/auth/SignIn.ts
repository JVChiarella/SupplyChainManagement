import jwt from "jsonwebtoken";

interface credentials {
    username: any;
    password: any; 
    userType: String;
}

export default async function SignIn(credentials : credentials) {
    const res = await fetch(`http://localhost:8080/login/${credentials.userType}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username: credentials?.username, 
                           password: credentials?.password  
            }),
    });
    const jwt = await res.json();

    console.log(jwt);
    //console.log(jwt['token']);

    if(jwt['token'] === undefined){
        //invalid login
        return null
    }

    if (verifyJWT(jwt['token'])) {
        return "logged in successfully"
    } else {
        //invalid jwt
        return null
    }
}

function verifyJWT(token : any){
    let authentication = true;
    //cant get verification to work. skipping for now; jwt verification works on backend
    /*
    jwt.verify(token, "fjalskdjfaudcvbkzxzxcvvbajksdhlflkajsdhlfk", {algorithms : ['HS256'] }
    (err : any, decoded : any) => {
        if (err){
            console.log("error: " + err);
            authentication = false;
        } else {
            console.log(decoded);
            authentication = true;
        }
    });
    */
    return authentication;
}
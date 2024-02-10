import { COOKIE_NAME } from "@/app/constants";
import { serialize } from "cookie";

interface Credentials {
    username: any;
    password: any; 
    userType: String;
}

export async function POST(request : Request) {
    const credentials : Credentials = await request.json();

    const res = await fetch(`http://localhost:8080/login/${credentials.userType}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username: credentials?.username, 
                           password: credentials?.password  
            }),
    });
    const jwt = await res.json();

    if(jwt['token'] === undefined){
        //invalid login
        return new Response(JSON.stringify("JWT validation failed"), {
            status: 401,
        })

    }

    //verify jwt by sending back to spring api (b/c node jsonwebtoken verify does not work)
    verifyJWT(jwt['token']);
    
    //serialize token as cookie and send back to be stored in browser
    const serialized = serialize(COOKIE_NAME, jwt['token'], {
        httpOnly: true,
        sameSite: 'strict',
        path: "/"
    });

    const response = {
        message: "user authenitcated"
    };

    return new Response(JSON.stringify(response), {
        status: 200,
        headers: { "Set-Cookie": serialized }
    })
}

async function verifyJWT(token : any){
    let authentication = true;
    const secret = process.env.SECRET_KEY || "";

    //add prefix to token
    const auth = "Bearer " + token;

    //verify jwt in spring (jsonwebtoken auth in node does not work?)
    const response = await fetch(`http://localhost:8080/verification`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' ,
                'Authorization': auth},
    })

    //check response from spring api
    const res = await response.json();
    if(res.message !== "verified successfully"){
        authentication = false;
    }

    return authentication;
}
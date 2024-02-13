import { cookies } from "next/headers";
import { COOKIE_NAME } from "@/app/constants";
import jwt from "jsonwebtoken";

export async function GET(){
    const cookieStore = cookies();

    const token = cookieStore.get(COOKIE_NAME);
    if(token){
        //add token prefix
        const auth = "Bearer " + token?.value;
    
        //verify jwt in spring
        const response = await fetch(`http://localhost:8080/verification`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' ,
                    'Authorization': auth},
        }).then(async (res) => {
                const result = await res.json();
                if(result.message == "verified successfully"){
                    //check if user is emplyoee or customer; access varies
                    const options : any = jwt.decode(token?.value)
                    if(options.sub == "employee"){
                        let obj1 = result;
                        let obj2 = {type: "employee"}
                        let newResponse = {...obj1, ...obj2};
                        return new Response(JSON.stringify(newResponse), {
                            status: 200,
                        })
                    } else {
                        let obj1 = result;
                        let obj2 = {type: "customer"}
                        let newResponse = {...obj1, ...obj2};
                        return new Response(JSON.stringify(newResponse), {
                            status: 200,
                        })
                    }
                }
            }, () => {
                return new Response(JSON.stringify("JWT validation failed"), {
                    status: 401,
                })
            })
    
        return response;
    } else {
        return new Response(JSON.stringify("JWT validation failed"), {
            status: 401,
        })
    }
}
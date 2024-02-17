import { cookies } from "next/headers";
import { COOKIE_NAME } from "@/app/constants";
import jwt from "jsonwebtoken";

//function to retrieve admin status from cookie for required requests
export async function GET(){
    const cookieStore = cookies();
    const token = cookieStore.get(COOKIE_NAME);
    if(token){
        const options : any = jwt.decode(token?.value)
        return new Response(JSON.stringify(options.admin), {
            status: 200,
        })
    } else {
        return new Response(JSON.stringify("jwt token not found"), {
            status: 401,
        })
    }
}
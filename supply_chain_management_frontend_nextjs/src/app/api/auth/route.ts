import { cookies } from "next/headers";
import { COOKIE_NAME } from "@/app/constants";
import { NextResponse } from "next/server";

export async function GET(){
    const cookieStore = cookies();

    const token = cookieStore.get(COOKIE_NAME);
    if(token){
        //verify jwt once working

        const response = {
            message: "token found and authenticated"
        };

        return new Response(JSON.stringify(response), {
            status: 200,
        })
    } else {
        return NextResponse.json(
            {
                message: "Unauthorized",
            },
            {
                status: 401,
            }
        );
    }
}
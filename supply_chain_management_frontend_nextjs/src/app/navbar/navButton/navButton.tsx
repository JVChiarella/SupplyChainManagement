import React from 'react'
import Link from 'next/link'

const NavButton = (props : any) => {
    if(props.name === "home"){
        return(
            <div className= "navbar-button">
                <Link href= {"./"}>{props.name}</Link>
            </div>
        )
    } else {
        return (
            <div className= "navbar-button">
                <Link href= {"./" + props.name}>{props.name}</Link>
            </div>
          )
    }
}


export default NavButton
import React from 'react'
import Link from 'next/link'

const NavButton = (props : any) => {
    return (
        <div className= "navbar-button">
            <button>
                <Link href= {"./" + props.name}>{props.name}</Link>
            </button>
        </div>
        )
}

export default NavButton
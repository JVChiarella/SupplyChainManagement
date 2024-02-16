import React from 'react'
import GetAllOrdersByCustomer from '../components/GetAllOrdersByCustomer'

const OrdersPage = () => {
  return (
    <div className = "list-container">
        <GetAllOrdersByCustomer></GetAllOrdersByCustomer>
    </div>
  )
}

export default OrdersPage
import React from 'react'
import GetAllOrdersByCustomer from '../components/GetAllOrdersByCustomer'
import AddOrder from '../components/AddOrder'

const OrdersPage = () => {
  return (
    <div>
        <GetAllOrdersByCustomer></GetAllOrdersByCustomer>
        <AddOrder></AddOrder>
    </div>
  )
}

export default OrdersPage
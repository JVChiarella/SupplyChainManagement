import Navbar from "../navbar/navbar";
import GetAllEmployees from "../components/GetAllEmployees";
import GetAllCustomers from "../components/GetAllCustomers";

const UsersPage = () => {
  return (
    <div>
      <Navbar></Navbar>
      <GetAllEmployees></GetAllEmployees>

      <div>-------------------------</div>

      <GetAllCustomers></GetAllCustomers>
    </div>
  )
}

export default UsersPage
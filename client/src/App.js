import Home from "./pages/Home";
import Dashboard from "./pages/dashboard/Dashboard";
import { Routes, Route, Navigate } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import {
  add as addAuthUser,
  remove as removeAuthUser,
} from "./store/slices/authUser";
import { remove as removeAccessToken } from "./store/slices/accessToken.js";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getPrincipal as getPrincipalUser } from "./api/users.js";
import useAccessToken from "./hooks/useAccessToken.js";

function App() {
  const authUser = useSelector((state) => state.au.v);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const accessToken = useAccessToken();
  const { fetch: originalFetch } = window;

  useEffect(() => {
    window.fetch = async (...args) => {
      let [resource, config] = args;
      const response = await originalFetch(resource, config);
      if ([403, 401].indexOf(response.status) > -1) {
        dispatch(removeAuthUser());
        dispatch(removeAccessToken());
        navigate("/");
      }
      return response;
    };

    (async () => {
      if (accessToken) {
        const { data } = await getPrincipalUser({ token: accessToken });
        if (data) {
          dispatch(addAuthUser(data));
          navigate("/dashboard");
          return;
        }
      }
    })();
  }, []);

  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute authUser={authUser}>
            <Dashboard />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

const ProtectedRoute = ({ authUser, children }) => {
  if (!authUser) {
    return <Navigate to="/" replace />;
  }
  return children;
};

export default App;

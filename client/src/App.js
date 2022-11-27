import Home from "./pages/Home";
import Dashboard from "./pages/dashboard/Dashboard";
import { Routes, Route, Navigate } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import {
  add as addAuthUser,
  remove as removeAuthUser,
} from "./store/slices/authUser";
import { remove as removeAccessToken } from "./store/slices/accessToken.js";
import { Fragment, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getPrincipal as getPrincipalUser } from "./api/users.js";
import useAccessToken from "./hooks/useAccessToken.js";
import PuffLoader from "react-spinners/PuffLoader.js";
import { chakraTheme } from "./theme.js";
import { Center } from "@chakra-ui/react";

function App() {
  const authUser = useSelector((state) => state.au.v);
  const [isLoading, setIsLoading] = useState(true);
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
          setIsLoading(false);
          return;
        }
      }
      setIsLoading(false);
    })();
  }, []);

  return (
    <Fragment>
      {isLoading && (
        <Center height="100vh">
          <PuffLoader color={chakraTheme.colors.primary} />
        </Center>
      )}
      {!isLoading && (
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
      )}
    </Fragment>
  );
}

const ProtectedRoute = ({ authUser, children }) => {
  if (!authUser) {
    return <Navigate to="/" replace />;
  }
  return children;
};

export default App;

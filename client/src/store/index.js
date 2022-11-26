import { createStore } from "@reduxjs/toolkit";

import { persistStore } from "redux-persist";
import persistCombineReducers from "redux-persist/es/persistCombineReducers";
import storage from "redux-persist/lib/storage";
import { REDUX_PERSIST_KEY } from "../config";
import { accessTokenSlice } from "./slices/accessToken.js";
import { authUserSlice } from "./slices/authUser.js";

const persistConfig = {
  key: REDUX_PERSIST_KEY,
  blacklist: ["au"],
  storage,
};

const root = persistCombineReducers(persistConfig, {
  au: authUserSlice.reducer,
  at: accessTokenSlice.reducer,
});

export const store = createStore(root);
export const persistor = persistStore(store);

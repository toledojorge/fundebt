import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  v: null,
};

export const accessTokenSlice = createSlice({
  name: "at",
  initialState,
  reducers: {
    add: (state, action) => {
      state.v = action.payload;
      return state;
    },
    remove: (state) => {
      state.v = null;
      return state;
    },
  },
});

export const { add, remove } = accessTokenSlice.actions;

export default accessTokenSlice.reducer;

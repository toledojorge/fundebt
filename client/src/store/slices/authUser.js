import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  v: null,
};

export const authUserSlice = createSlice({
  name: "au",
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

export const { add, remove } = authUserSlice.actions;

export default authUserSlice.reducer;

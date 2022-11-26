import { useSelector } from "react-redux";

export default function useAccessToken() {
  const accessToken = useSelector((state) => state.at.v);
  return accessToken;
}

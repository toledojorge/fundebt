import {
  SimpleGrid,
  Box,
  Container,
  FormLabel,
  Center,
  FormControl,
  Input,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  Image,
  Button,
  Heading,
  Text,
  FormHelperText,
  Alert,
  AlertDescription,
} from "@chakra-ui/react";
import { ChevronDownIcon } from "@chakra-ui/icons";
import LoadingButton from "../components/LoadingButton";
import { useEffect, useState } from "react";
import { getAll as getAllUsers, signIn } from "../api/users";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { add as addAuthUser } from "../store/slices/authUser";
import { add as addAccessToken } from "../store/slices/accessToken.js";
import { chakraTheme } from "../theme.js";

export default function Home() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [isFormSubmitting, setIsFormSubmitting] = useState();
  const [users, setUsers] = useState([]);
  const [menuValue, setMenuValue] = useState("");
  const [error, setError] = useState(null);

  async function handleSubmit(e) {
    setIsFormSubmitting(true);
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData);
    if (data.email.length < 1 || data.password.length < 1) {
      setError("Please fill all the fields");
      setIsFormSubmitting(false);
      return;
    }
    const { data: res, error } = await signIn({
      body: data,
    });

    if (error) {
      setError(error);
      setIsFormSubmitting(false);
      return;
    }
    setIsFormSubmitting(false);
    dispatch(addAccessToken(res.accessToken));
    dispatch(addAuthUser(users.find((u) => u.email === data.email)));
    navigate("/dashboard");
  }

  useEffect(() => {
    (async function () {
      const { data: users } = await getAllUsers();
      setUsers(users);
    })();
  }, []);

  return (
    <Container
      display="flex"
      flexDirection="column"
      justifyContent="center"
      height="100%"
    >
      <Center
        p={5}
        boxShadow="base"
        height={["auto", "520px", "520px"]}
        width="100%"
      >
        <SimpleGrid width="100%" columns={1}>
          <Heading textAlign="center" mt={2}>
            Sign In
          </Heading>
          <Text align="center" mb={5} mt={2} fontSize="sm">
            To continue, please sign in. Select an available user and input the
            credentials
          </Text>
          <form onSubmit={(e) => handleSubmit(e)}>
            <Box width="100%">
              <Menu width="100%">
                <MenuButton
                  width="100%"
                  as={Button}
                  rightIcon={<ChevronDownIcon />}
                >
                  Select an user to get started!
                </MenuButton>
                <MenuList width="100%">
                  {users.map((u, i) => {
                    return (
                      <MenuItem
                        onClick={() => {
                          setMenuValue(u.email);
                        }}
                        key={i}
                        minH="48px"
                      >
                        <Image
                          boxSize="2rem"
                          borderRadius="full"
                          src={u.avatarUrl}
                          mr="12px"
                        />
                        <span>{u.fullName}</span>
                      </MenuItem>
                    );
                  })}
                </MenuList>
              </Menu>
            </Box>
            <Box mt={5}>
              <FormControl mb={2}>
                <FormLabel>Email</FormLabel>
                <Input
                  focusBorderColor={chakraTheme.colors.primary}
                  value={menuValue}
                  name="email"
                  type="text"
                  readOnly
                />
                <FormHelperText mb={3}>
                  This field is disabled, please use the above selector to fill
                  it with the email of the selected user
                </FormHelperText>
              </FormControl>
              <FormControl>
                <FormLabel>Password</FormLabel>
                <Input
                  focusBorderColor={chakraTheme.colors.primary}
                  name="password"
                  type="password"
                />
              </FormControl>
            </Box>
            {error && (
              <Alert mt={3} status="error">
                <AlertDescription>{error}</AlertDescription>
              </Alert>
            )}
            <Center>
              <LoadingButton
                width="100%"
                type="submit"
                mt={5}
                isLoading={isFormSubmitting}
                buttonTxt="Sign in"
              />
            </Center>
          </form>
        </SimpleGrid>
      </Center>
    </Container>
  );
}

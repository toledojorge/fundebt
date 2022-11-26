import { SmallCloseIcon } from "@chakra-ui/icons";
import {
  Box,
  Button,
  Drawer,
  DrawerOverlay,
  DrawerCloseButton,
  DrawerHeader,
  DrawerBody,
  DrawerContent,
  VStack,
  Grid,
  GridItem,
  Avatar,
  Divider,
  Text,
} from "@chakra-ui/react";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { remove as removeAccessToken } from "../store/slices/accessToken.js";
import { remove as removeAuthUser } from "../store/slices/authUser";

function SidebarContent() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  return (
    <VStack justifyContent="flex-end" height="100%">
      <Button
        display="flex"
        justifyContent="flex-start"
        colorScheme="red"
        leftIcon={<SmallCloseIcon />}
        onClick={() => {
          dispatch(removeAuthUser());
          dispatch(removeAccessToken());
          navigate("/");
        }}
        w="100%"
      >
        Logout
      </Button>
    </VStack>
  );
}

export default function Sidebar({ isOpen, variant, onClose }) {
  const authUser = useSelector((state) => state.au.v);
  return variant === "sidebar" ? (
    <Box
      position="fixed"
      left={0}
      p={5}
      w="200px"
      top={0}
      h="100%"
      bg="#dfdfdf"
    >
      <SidebarContent />
    </Box>
  ) : (
    <Drawer isOpen={isOpen} placement="left" onClose={onClose}>
      <DrawerOverlay>
        <DrawerContent>
          <DrawerCloseButton />
          <DrawerHeader>
            <Grid alignItems="center" templateColumns="repeat(12, 1fr)">
              <GridItem colSpan={1}>
                <Avatar src={authUser.avatarUrl} />
              </GridItem>
              <GridItem ml={2} colSpan={11}>
                <Text>{authUser.fullName}</Text>
                <Text color="gray.500" fontSize="sm">
                  {authUser.email}
                </Text>
              </GridItem>
            </Grid>
            <Divider mt={3} />
          </DrawerHeader>
          <DrawerBody>
            <SidebarContent />
          </DrawerBody>
        </DrawerContent>
      </DrawerOverlay>
    </Drawer>
  );
}

import { Center, Flex, Text } from "@chakra-ui/react";
import React from "react";

export class GlobalExceptionHandler extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      hasError: false,
      errorMessage: "",
    };
  }
  componentDidCatch(error) {
    this.setState({ hasError: true });
    this.setState({ errorMessage: error.message });
  }
  render() {
    if (this.state?.hasError) {
      return (
        <Center p={2} textAlign="center" height="100vh">
          <Flex direction="column" alignItems="center">
            <Text fontSize="2xl">Oops... Something went wrong 🦝</Text>
            <Text color="gray.500">
              <i>
                There's probably a problem with the server, please try again
                later.
              </i>
            </Text>
          </Flex>
        </Center>
      );
    }
    return this.props.children;
  }
}

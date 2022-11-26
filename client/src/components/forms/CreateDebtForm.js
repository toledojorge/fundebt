import {
  Text,
  FormControl,
  FormLabel,
  Input,
  Alert,
  AlertDescription,
  Heading,
} from "@chakra-ui/react";
import { create as createDebt } from "../../api/debts";
import { CURRENCY } from "../../config";
import LoadingButton from "../LoadingButton";
import { useState } from "react";
import { AddIcon } from "@chakra-ui/icons";
import useAccessToken from "../../hooks/useAccessToken";

export default function CreateDebtForm({ onSuccess, debtUserId }) {
  const accessToken = useAccessToken();
  const [error, setError] = useState(null);
  const [isFormSubmitting, setIsFormSubmitting] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setIsFormSubmitting(true);
    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData);
    if (data.amount.length < 1 || data.description.length < 1) {
      setError("Please fill all the fields");
      setIsFormSubmitting(false);
      return;
    }
    const { error } = await createDebt({
      body: {
        ...data,
        debtUserId,
      },
      token: accessToken,
    });

    e.target.reset();

    if (error) {
      setError(error);
      setIsFormSubmitting(false);
      return;
    }

    setIsFormSubmitting(false);
    setError(null);
    onSuccess();
  }

  return (
    <form onSubmit={handleSubmit}>
      <Heading size="md">Create a new debt</Heading>
      <FormControl mt={5}>
        <FormLabel>Amount ({CURRENCY})</FormLabel>
        <Input name="amount" step="0.01" type="number" />
      </FormControl>
      <FormControl mt={3}>
        <FormLabel>Description</FormLabel>
        <Input name="description" type="text" />
      </FormControl>
      {error && (
        <Alert mt={3} status="error">
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      )}
      <LoadingButton
        type="submit"
        mt={5}
        mb={5}
        leftIcon={<AddIcon />}
        isLoading={isFormSubmitting}
        buttonTxt="Submit"
      />
    </form>
  );
}

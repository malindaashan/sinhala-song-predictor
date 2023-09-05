import React from "react";
import Box from "@mui/material/Box";
import {Button, FormControl, Grid, InputLabel, MenuItem, Select} from "@mui/material";
import SongUploader from "../common/SongUploader";
import MLPredictorService from "../../service/MLPredictorService";

const MLPredictor = () => {
    const [selectedAlgorithm, setSelectedAlgorithm] = React.useState("Random-Forest")
    const [selectedFile, setSelectedFile] = React.useState();
    const [result, setResult] = React.useState(null);
    const handleClassifierAlgoChange = (event) => {
        setSelectedAlgorithm(event.target.value)
    }

    function executeClassifier() {
        setResult("Loading.....")
        MLPredictorService.executeClassifier(selectedAlgorithm, selectedFile)
            .then((response) => {
                if (response.success) {
                    setResult(response.data);
                } else {
                    alert("Failed");
                }
            })
            .catch((ex) => {
                console.log("error")
            });
    }

    return (
        <Box id="ml-predictor" className="ml-box">
            <h1> Classifier</h1>
            <br/><br/><br/>
            <Grid container>
                <Grid item xs={12} sm={6} md={3}>
                    <FormControl fullWidth sx={{paddingRight: '40px'}}>
                        <InputLabel id="cluster-lbl">Choose Classifier Algorithm</InputLabel>
                        <Select
                            labelId="classifier-lbl"
                            id="classifier-select"
                            label="Choose Classifier Algorithm"
                            value={selectedAlgorithm}
                            onChange={handleClassifierAlgoChange}
                        >
                            <MenuItem value="Random-Forest">Random-Forest</MenuItem>
                        </Select>
                    </FormControl>
                </Grid>
            </Grid>
            <br/>
            <Grid id={'song-uploader'} container justifyContent="center">
                <Grid item xs={12} sm={6} md={3}>
                    <SongUploader
                        setSelectedFile={setSelectedFile}
                    />
                </Grid>
            </Grid>
            <Grid container justifyContent="flex-end">
                <Button id="clusterer-button" size="medium" variant="contained" color='secondary'
                        style={{marginTop: "20px"}} onClick={() => executeClassifier()}>CLASSIFY</Button>
            </Grid>
            <Grid container justifyContent="center">
                <h1>Result is: {result}</h1>

            </Grid>
        </Box>
    )

}

export default MLPredictor;
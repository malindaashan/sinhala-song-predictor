import * as React from 'react';
import Box from "@mui/material/Box";
import Modal from "@mui/material/Modal";

const Loader = ({downloadSuccessful,showDownloader}) => {
    const style = {
        position: 'absolute',
        top: '50%',
        left: '30%',
        transform: 'translate(-50%, -50%)',
        width: '10vh'
    };
    return (
        <Modal
            open={showDownloader}
        >
            <Box sx={{...style}}>
                <img src={process.env.PUBLIC_URL + '/downloading.gif'} width='700%' id="loader-img" alt="loader"/>
            </Box>
        </Modal>
    );
}

export default Loader